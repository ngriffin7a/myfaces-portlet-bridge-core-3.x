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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.attr.ext.LayoutRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.ListenerRenderKit;
import net.sourceforge.myfaces.renderkit.html.util.RendererListener;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LayoutRenderer
    extends HTMLRenderer
    implements LayoutRendererAttributes,
               RendererListener
{
    static final String HEADER = "LayoutHeader";
    static final String NAVIGATION = "LayoutNavigation";
    static final String BODY = "LayoutBody";
    static final String FOOTER = "LayoutFooter";

    private static final String BEGIN_TOKEN_PREFIX = "[";
    private static final String BEGIN_TOKEN_SUFFIX = "-BEGIN]";
    private static final String END_TOKEN_PREFIX = "[";
    private static final String END_TOKEN_SUFFIX = "-END]";

    public static final String BODY_CONTENT_REQUEST_ATTR = LayoutRenderer.class.getName() + ".BODY_CONTENT";


    public static final String CLASSIC_LAYOUT = "classic";
    public static final String NAV_RIGHT_LAYOUT = "navigationRight";


    public static final String TYPE = "Layout";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent component)
    {
        return component instanceof UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIPanel.TYPE);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ListenerRenderKit.addChildrenListener(facesContext, uiComponent, this);
    }


    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UIPanel.TYPE))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            if (uiComponent.getAttribute(HEADER_CLASS_ATTR) != null)
            {
                writer.write(beginToken(LayoutRenderer.HEADER));
            }
            else if (uiComponent.getAttribute(NAVIGATION_CLASS_ATTR) != null ||
                     (uiComponent.getRendererType() != null &&
                      uiComponent.getRendererType().equals(NavigationRenderer.TYPE)))
            {
                writer.write(beginToken(LayoutRenderer.NAVIGATION));
            }
            else if (uiComponent.getAttribute(BODY_CLASS_ATTR) != null)
            {
                writer.write(beginToken(LayoutRenderer.BODY));
            }
            else if (uiComponent.getAttribute(FOOTER_CLASS_ATTR) != null)
            {
                writer.write(beginToken(LayoutRenderer.FOOTER));
            }
        }
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UIPanel.TYPE))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            if (uiComponent.getAttribute(HEADER_CLASS_ATTR) != null)
            {
                writer.write(endToken(LayoutRenderer.HEADER));
            }
            else if (uiComponent.getAttribute(NAVIGATION_CLASS_ATTR) != null ||
                (uiComponent.getRendererType() != null &&
                uiComponent.getRendererType().equals(NavigationRenderer.TYPE)))
            {
                writer.write(endToken(LayoutRenderer.NAVIGATION));
            }
            else if (uiComponent.getAttribute(BODY_CLASS_ATTR) != null)
            {
                writer.write(endToken(LayoutRenderer.BODY));
            }
            else if (uiComponent.getAttribute(FOOTER_CLASS_ATTR) != null)
            {
                writer.write(endToken(LayoutRenderer.FOOTER));
            }
        }
    }

    protected static String beginToken(String part)
    {
        return BEGIN_TOKEN_PREFIX + part + BEGIN_TOKEN_SUFFIX;
    }

    protected static String endToken(String part)
    {
        return END_TOKEN_PREFIX + part + END_TOKEN_SUFFIX;
    }





    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        writeBody(facesContext, uiComponent);
        ListenerRenderKit.removeListener(facesContext, uiComponent, this);
    }




    protected void writeBody(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        BodyContent bodyContent = (BodyContent)facesContext.getServletRequest()
            .getAttribute(BODY_CONTENT_REQUEST_ATTR);
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }

        String layout = (String)uiComponent.getAttribute(LAYOUT_ATTR);
        if (layout == null)
        {
            LogUtil.getLogger().severe("No layout attribute!");
            ResponseWriter writer = facesContext.getResponseWriter();
            bodyContent.writeOut(writer);
            return;
        }

        if (layout.equals(CLASSIC_LAYOUT))
        {
            writeClassicLayout(facesContext, uiComponent, bodyContent);
        }
        else if (layout.equals(NAV_RIGHT_LAYOUT))
        {
            writeNavRightLayout(facesContext, uiComponent, bodyContent);
        }
        else
        {
            LogUtil.getLogger().severe("Layout '" + layout + "' not supported.");
            ResponseWriter writer = facesContext.getResponseWriter();
            bodyContent.writeOut(writer);
            return;
        }
    }


    protected String findChildClassAttribute(UIComponent uiComponent,
                                             String cssClassAttribute)
    {
        for (Iterator it = uiComponent.getChildren(); it.hasNext();)
        {
            String cssClass = (String)((UIComponent)it.next()).getAttribute(cssClassAttribute);
            if (cssClass != null)
            {
                return cssClass;
            }
        }
        return null;
    }


    protected void writePartAsTd(ResponseWriter writer,
                                 BodyContent bodyContent,
                                 String part,
                                 UIComponent uiComponent,
                                 String cssClassAttribute,
                                 int colSpan)
        throws IOException
    {
        writer.write("<td colspan=\"" + colSpan +"\"");
        String cssClass = findChildClassAttribute(uiComponent, cssClassAttribute);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writePart(writer, bodyContent, part);
        writer.write("</td>");
    }



    protected void writePart(ResponseWriter writer,
                             BodyContent bodyContent,
                             String part)
        throws IOException
    {
        String bodyString = bodyContent.getString();

        String beginToken = beginToken(part);

        int startIdx = bodyString.indexOf(beginToken);
        if (startIdx == -1)
        {
            //Part not present
            return;
        }

        int endIdx = bodyString.indexOf(endToken(part), startIdx);
        if (endIdx < startIdx)
        {
            throw new IllegalArgumentException("Curious tokens.");
        }

        writer.write(bodyString.substring(startIdx + beginToken.length(), endIdx));
    }



    protected void writeClassicLayout(FacesContext facesContext,
                                      UIComponent uiComponent,
                                      BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table");
        String cssClass = (String)uiComponent.getAttribute(PANEL_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, HEADER, uiComponent, HEADER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, NAVIGATION, uiComponent, NAVIGATION_CLASS_ATTR, 1);
        writePartAsTd(writer, bodyContent, BODY, uiComponent, BODY_CLASS_ATTR, 1);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, FOOTER, uiComponent, FOOTER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("</table>");
    }

    protected void writeNavRightLayout(FacesContext facesContext,
                                       UIComponent uiComponent,
                                       BodyContent bodyContent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table");
        String cssClass = (String)uiComponent.getAttribute(PANEL_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"" + cssClass + "\"");
        }
        writer.write(">");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, HEADER, uiComponent, HEADER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, BODY, uiComponent, BODY_CLASS_ATTR, 1);
        writePartAsTd(writer, bodyContent, NAVIGATION, uiComponent, NAVIGATION_CLASS_ATTR, 1);
        writer.write("</tr>");
        writer.write("<tr>");
        writePartAsTd(writer, bodyContent, FOOTER, uiComponent, FOOTER_CLASS_ATTR, 2);
        writer.write("</tr>");
        writer.write("</table>");
    }



}
