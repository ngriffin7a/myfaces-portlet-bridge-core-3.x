/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit.attr;

/**
 * Constant definitions for the specified render dependent attributes of the
 * "Message" renderer type.
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface LabelRendererAttributes
    extends CommonRendererAttributes
{
    public static final String KEY_ATTR = "key";
    public static final String BUNDLE_ATTR = "bundle";

    public static final String FOR_ATTR = "for";
    public static final String ACCESSKEY_ATTR = "accesskey";
    public static final String ONBLUR_ATTR = "onblur";
    public static final String ONFOKUS_ATTR = "onfocus";
    public static final String[] COMMON_LABEL_ATTRIBUTES =
    {
        FOR_ATTR,
        ACCESSKEY_ATTR,
        ONBLUR_ATTR,
        ONFOKUS_ATTR
    };

}
