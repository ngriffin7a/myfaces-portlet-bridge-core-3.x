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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.UICommand;
import net.sourceforge.myfaces.renderkit.html.HyperlinkRenderer;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CommandHyperlinkTag
        extends MyFacesBodyTag
{
    //MyFaces tag extensions:
    public UIComponent createComponent()
    {
        return new UICommand();
    }

    public String getRendererType()
    {
        return HyperlinkRenderer.TYPE;
    }


    //Iteration Tag support
    public int getDoAfterBodyValue() throws JspException
    {

        return super.getDoAfterBodyValue();
    }


    public void setCommandName(String v)
    {
        setValue(v);
    }

    public void setCommandReference(String v)
    {
        setComponentAttribute(UICommand.COMMAND_REFERENCE_ATTR, v);
    }

    public void setHref(String v)
    {
        setRendererAttribute(HyperlinkRenderer.HREF_ATTR, v);
    }

    public void setCommandClass(String v)
    {
        setRendererAttribute(HyperlinkRenderer.COMMAND_CLASS_ATTR, v);
    }

    public void setAccesskey(String value)
    {
        setRendererAttribute(HyperlinkRenderer.ACCESSKEY_ATTR, value);
    }

    public void setCharset(String value)
    {
        setRendererAttribute(HyperlinkRenderer.CHARSET_ATTR, value);
    }

    public void setCoords(String value)
    {
        setRendererAttribute(HyperlinkRenderer.COORDS_ATTR, value);
    }

    public void setHreflang(String value)
    {
        setRendererAttribute(HyperlinkRenderer.HREFLANG_ATTR, value);
    }

    public void setRel(String value)
    {
        setRendererAttribute(HyperlinkRenderer.REL_ATTR, value);
    }

    public void setRev(String value)
    {
        setRendererAttribute(HyperlinkRenderer.REV_ATTR, value);
    }

    public void setShape(String value)
    {
        setRendererAttribute(HyperlinkRenderer.SHAPE_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttribute(HyperlinkRenderer.TABINDEX_ATTR, value);
    }

    public void setTarget(String value)
    {
        setRendererAttribute(HyperlinkRenderer.TARGET_ATTR, value);
    }

    public void setType(String value)
    {
        setRendererAttribute(HyperlinkRenderer.TYPE_ATTR, value);
    }
}
