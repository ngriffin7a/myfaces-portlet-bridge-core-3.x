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
package net.sourceforge.myfaces.renderkit.attr.ext;

import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.KeyBundleAttributes;

/**
 * Constant definitions for the specified render dependent attributes of the
 * "Navigation" renderer type.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface NavigationRendererAttributes
    extends CommonRendererAttributes,
            KeyBundleAttributes
{
    //public static final String LEVEL_CLASSES = "levelClasses";
    public static final String OPEN_ITEM_CLASS_ATTR = "openItemClass";
    public static final String ACTIVE_ITEM_CLASS_ATTR = "activeItemClass";
    public static final String ITEM_CLASS_ATTR = "itemClass";
    public static final String SEPARATOR_CLASS = "separatorClass";

    public static final String[] NAVIGATION_ATTRIBUTES = {
        PANEL_CLASS_ATTR,
        BUNDLE_ATTR,
        //LEVEL_CLASSES,
        OPEN_ITEM_CLASS_ATTR,
        ACTIVE_ITEM_CLASS_ATTR,
        ITEM_CLASS_ATTR,
        SEPARATOR_CLASS
    };
}
