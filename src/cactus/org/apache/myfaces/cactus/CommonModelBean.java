/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package net.sourceforge.myfaces.cactus;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/06/28 22:12:11  o_rossmueller
 * fix #978654: do not coerce null
 *
 * Revision 1.2  2004/05/26 17:19:56  o_rossmueller
 * test for bug 948626
 *
 * Revision 1.1  2004/04/23 15:12:14  manolito
 * inputHidden bug reported by Travis
 *
 */
public class CommonModelBean
{
    //private static final Log log = LogFactory.getLog(CommonModelBean.class);

    private long _primitiveLong = 0;
    private boolean primitiveLongSet = false;
    private Integer integer = null;
    private String string;


    public String getString()
    {
        return string;
    }


    public void setString(String string)
    {
        this.string = string;
    }


    public long getPrimitiveLong()
    {
        return _primitiveLong;
    }

    public void setPrimitiveLong(long primitiveLong)
    {
        _primitiveLong = primitiveLong;
        primitiveLongSet = true;
    }


    public boolean isPrimitiveLongSet()
    {
        return primitiveLongSet;
    }


    public Integer getInteger()
    {
        return integer;
    }


    public void setInteger(Integer integer)
    {
        this.integer = integer;
    }
}
