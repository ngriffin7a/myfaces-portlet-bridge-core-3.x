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
package net.sourceforge.myfaces.taglib.html.ext;

import net.sourceforge.myfaces.component.html.ext.HtmlMessages;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.html.HtmlMessagesTagBase;

import javax.faces.component.UIComponent;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/04/01 14:34:23  manolito
 * new globalSummaryFormat attribute
 *
 * Revision 1.3  2004/04/01 12:57:42  manolito
 * additional extended component classes for user role support
 *
 */
public class HtmlMessagesTag
        extends HtmlMessagesTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlOutputFormatTag.class);

    public String getComponentType()
    {
        return HtmlMessages.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.Messages";
    }

    private String _summaryFormat;
    private String _globalSummaryFormat;
    private String _detailFormat;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "summaryFormat", _summaryFormat);
        setStringProperty(component, "globalSummaryFormat", _globalSummaryFormat);
        setStringProperty(component, "detailFormat", _detailFormat);
        setStringProperty(component, JSFAttr.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, JSFAttr.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public void setGlobalSummaryFormat(String globalSummaryFormat)
    {
        _globalSummaryFormat = globalSummaryFormat;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}
