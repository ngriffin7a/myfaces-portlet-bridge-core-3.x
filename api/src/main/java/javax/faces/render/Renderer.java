/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.faces.render;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 * see Javadoc of <a href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/api/index.html">JSF Specification</a>
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class Renderer
{
    public void decode(FacesContext context,
                       UIComponent component)
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public void encodeBegin(FacesContext context,
                            UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    /**Render all children if there are any.
     * 
     * Note: this will only be called if getRendersChildren()
     * returns true. A component which has a renderer with
     * getRendersChildren() set to true will typically contain
     * the rendering logic for its children in this method.
     * 
     * @param context
     * @param component
     * @throws IOException
     */    
    public void encodeChildren(FacesContext context,
                               UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
        
        List children = component.getChildren();
        for (int i=0; i<children.size(); i++) 
        {
            UIComponent child = (UIComponent) children.get(i);
            
            if (!child.isRendered())
            {
                continue;
            }

            child.encodeBegin(context);
            if (child.getRendersChildren())
            {
                child.encodeChildren(context);
            }
            else {
              encodeChildren(context, child);
            }
            child.encodeEnd(context);
        }
    }

    
    public void encodeEnd(FacesContext context,
                          UIComponent component)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
    }

    public String convertClientId(FacesContext context,
                                  String clientId)
    {
        if (context == null) throw new NullPointerException("context");
        if (clientId == null) throw new NullPointerException("clientId");
        return clientId;
    }

    /**Switch for deciding who renders the children.
     * 
     * @return <b>true</b> - if the component takes care of rendering its
     * children. In this case, encodeChildren() ought to be called
     * by the rendering controller (e.g., the rendering controller
     * could be the component's JSP-Tag). 
     * In the method encodeChildren(), the component
     * should therefore provide all children encode logic.
     * <br/>
     * <b>false</b> - if the component does not take care of rendering its
     * children. In this case, encodeChildren() should not be called
     * by the rendering controller. Instead, the children-list should
     * be retrieved and the children should directly be rendered by
     * the rendering controller one by one.
     */    
    public boolean getRendersChildren()
    {
        return false;
    }

    public Object getConvertedValue(FacesContext context,
                                    UIComponent component,
                                    Object submittedValue)
            throws ConverterException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");
        return submittedValue;
    }

}
