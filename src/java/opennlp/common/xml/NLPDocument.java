///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2000 Jason Baldridge and Gann Bierner
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////

package opennlp.common.xml;

import opennlp.common.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.io.*;
import java.util.*;


/**
 * A class which wraps an XmlDocument inside and ensures that it fits OpenNLP
 * specifications.
 *
 * @author      Jason Baldridge
 * @version     $Revision: 1.6 $, $Date: 2001/11/29 13:12:05 $
 **/
public class NLPDocument extends Document {
    public static final String WORD_LABEL = "w";
    public static final String TOKEN_LABEL = "t";
    public static final String SENTENCE_LABEL = "s";
    public static final String PARAGRAPH_LABEL = "p";
    
    private Element _root;
    private int _textLength = 0;
    
    /**
     * Empty arg constructor provided for those who do not want the default
     * behavior of the constructor which takes a string argument.  Creates the
     * root element "nlpDocument" and nothing else.
     **/
    public NLPDocument () {
	setRootElement(new Element("nlpDocument"));
    }

    
   /**
    * Constructor which creates an NLPDocument object from a given text.  It
    * automatically breaks the text into paragraphs and "quasiWords" (chunks
    * of text separated by white spaces).  If this behavior is not desired,
    * use the empty arg constructor and build up the document from the root
    * element.
    *
    * @param text The text which is to be placed into this NLPDocument and
    * manipulated by tools which process NLPDocuments.
    **/
    public NLPDocument (String text) {
	this();
	_textLength = text.length();
	Element textEl = new Element("text");
	String[] paragraphs, quasiWords;
	    
	paragraphs = PerlHelp.getParagraphs(text);
	for (int i=0; i<paragraphs.length; i++) {
	    Element paraEl = new Element(PARAGRAPH_LABEL);
	    Element sentEl = new Element(SENTENCE_LABEL);
	    quasiWords = PerlHelp.splitByWhitespace(paragraphs[i]);
	    for (int j=0; j<quasiWords.length; j++) {
		Element tokenEl = new Element(TOKEN_LABEL);
		Element wordEl = new Element(WORD_LABEL);
		wordEl.setText(quasiWords[j]);
		tokenEl.addContent(wordEl);
		sentEl.addContent(tokenEl);
	    }
	    paraEl.addContent(sentEl);
	    textEl.addContent(paraEl);
	}
	_root.addContent(textEl);
    }

    /**
     * Sets the root element of this document while maintaining the pointer
     * which the NLPDocument object keeps to the root Element.
     **/
    public Document setRootElement (Element root) {
	_root = root;
	return super.setRootElement(root);
    }
    
    
    /**
     *  Builds a String[] with each item being a paragraph.
     **/
    public String[] getParagraphs () {
	List parEls = getParagraphElements();
	String[] pars = new String[parEls.size()];
	int index = 0;
	for (Iterator i=parEls.iterator(); i.hasNext();) {
	    StringBuffer sb = new StringBuffer();
	    String[] sents = getSentences((Element)i.next());
	    for (int j=0; j<sents.length; j++)
		sb.append(sents[j]).append(' ');
	    pars[index++] = sb.toString().trim();
	}
	return pars;
    }

    
    /**
     * Grabs all paragraph elements in this document.
     **/
    public List getParagraphElements () {
	return XmlUtils.getChildrenNested(_root, PARAGRAPH_LABEL);
    }

    
    /**
     * Grabs all sentence elements in this document.
     **/
    public String[] getSentences () {
	return getSentences(_root);
    }

    
    /**
     * Builds a String[] with each item being a sentence.
     **/
    public String[] getSentences (Element e) {
	List sentEls = getSentenceElements(e);
	String[] sents = new String[sentEls.size()];
	int index = 0;
	for (Iterator i=sentEls.iterator(); i.hasNext();) {
	    List tokEls = getTokenElements((Element)i.next());
	    StringBuffer sent = new StringBuffer();
	    for (Iterator j=tokEls.iterator(); j.hasNext();) {
		List wordEls = getWordElements((Element)j.next());
		if (wordEls.size() == 1) {
		    sent.append(((Element)wordEls.get(0)).getText()).append(' ');
		}
		else {
		    for (Iterator k=wordEls.iterator(); k.hasNext();)
			sent.append(((Element)k.next()).getText()).append(' ');
		}
	    }	    
	    sents[index++] = sent.toString().trim();
	}
	return sents;
    }

    
    /**
     * Grabs all sentence elements below the given element.
     **/
    public List getSentenceElements (Element e) {
	return XmlUtils.getChildrenNested(e, SENTENCE_LABEL);
    }

    /**
     * Grabs all sentence elements in this document.
     **/
    public List getSentenceElements () {
	return getSentenceElements(_root);
    }
    
    /**
     * Grabs all token elements below the given element;
     **/
    public List getTokenElements (Element e) {
	return XmlUtils.getChildrenNested(e, TOKEN_LABEL);
    }
    
    /**
     * Grabs all token elements in this document.
     **/
    public List getTokenElements () {
	return getTokenElements(_root);
    }

    /**
     * Grabs all word elements below the given element;
     **/
    public List getWordElements (Element e) {
	return XmlUtils.getChildrenNested(e, WORD_LABEL);
    }
    
    /**
     * Grabs all word elements in this document.
     **/
    public List getWordElements () {
	return getWordElements(_root);
    }
    
    /**
     * Creates a nicely indented String showing the XML content of this
     * NLPDocument.
     **/
    public String toXml () {
        StringWriter sw = new StringWriter();
	
          try {
              new XMLOutputter("  ", true).output(this, sw);
          }
          catch (Exception e) {
              System.out.println("Unable to print document.");
              System.out.println(e);
          }
	  
	  return sw.toString();
    }


    /**
     * Outputs the normalized text content of this NLPDocument.
     **/
    public String toString () {
	StringBuffer sb = new StringBuffer(_textLength);
	List parEls = getParagraphElements();
	for (Iterator i=parEls.iterator(); i.hasNext();) {
	    String[] sents = getSentences((Element)i.next());
	    for (int j=0; j<sents.length; j++)
		sb.append(sents[j]);
	    sb.append("\n\n");
	}
	return sb.toString().trim();
    }


    /**
     * Create a SENT element for an NLP Document.
     * 
     * @param sent the string of text associated with this sentence.
     **/
    public static Element createSENT(String sent) {
        int l = sent.length();
        Element sentEl = new Element(SENTENCE_LABEL);
        if (l>0) {
            char delim = sent.charAt(l-1);
            if(delim == '.' || delim == '?' || delim == '!') {
                sentEl.addContent(createTOK(sent.substring(0,l-1)));
                sentEl.addContent(createTOK(""+delim));
            }
	    else {
                sentEl.addContent(createTOK(sent));
	    }
        }
	return sentEl;
    }


    /**
     * Create a TOK element for an NLPDocument.
     * 
     * @param tok the string of text associated with this token.
     **/
    public static Element createTOK(String tok) {
        return
	    new Element(TOKEN_LABEL).addContent(
					new Element(WORD_LABEL).setText(tok));
    }


    public static void main (String[] args) {
	NLPDocument doc = new NLPDocument("Here is a sentence. And this is another one.\n\nThis is a sentence in a new paragraph.");
	//System.out.println(doc.toString());
	System.out.println(doc.toXml());
    }
}
