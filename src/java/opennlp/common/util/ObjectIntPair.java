///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2002 Jason Baldridge
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////

package opennlp.common.util;

/**
 * A an object holding an Object and an int.
 *
 * @author      Jason Baldridge
 * @version     $Revision: 1.2 $, $Date: 2002/02/28 13:11:43 $
 */

public final class ObjectIntPair {
    public final Object a;
    public final int b;

    public ObjectIntPair (Object _a, int _b) {
	a=_a;
	b=_b;
    }

    public String toString () {
	return "["+a+"/"+b+"]";
    }

}
