///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2006 Calcucare GmbH
// 
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
// 
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
// 
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////

package opennlp.tools.doccat;

/**
*
* @author <a href="mailto:kottmann@gmail.com">Joern Kottmann</a>
* @version $Revision: 1.2 $, $Date: 2006/11/17 13:31:01 $
*/
public interface DocumentCategorizer {

  /**
   * Categorizes the given text. 
   * 
   * @param text
   */
  public double[] categorize(String text[]);

  public String getBestCategory(double[] outcome);

  public int getIndex(String category);

  public String getCategory(int index);

  public int getNumberOfCategories();

  public double[] categorize(String documentText);

  public String getAllResults(double results[]);
}