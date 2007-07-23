/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.taglib.core;

import javax.faces.webapp.UIComponentELTag;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.apache.myfaces.application.jsp.ViewResponseWrapper;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SubviewTag extends UIComponentELTag
{
    public SubviewTag() {
        super();
    }
    
    public String getComponentType()
    {
        return UINamingContainer.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }
    
    /**
     * Creates a UIComponent from the BodyContent
     * If a Subview is included via the <jsp:include> tag
     * the corresponding jsp is rendered with
     * getServletContext().getRequestDispatcher("includedSite").include(request,response)
     * and it is possible that something was written to the Response direct.
     * So is is necessary that the content of the wrapped response is added to the componenttree.
     * @return UIComponent or null
     */
    protected UIComponent createVerbatimComponentFromBodyContent() {
        UIOutput component = (UIOutput) super.createVerbatimComponentFromBodyContent();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object response = facesContext.getExternalContext().getResponse();
        String wrappedOutput;

        if (response instanceof ViewResponseWrapper)
        {
            ViewResponseWrapper wrappedResponse = (ViewResponseWrapper) response;
            wrappedOutput = wrappedResponse.toString();
            if (wrappedOutput != null && wrappedOutput.length() > 0)
            {
                String componentvalue = null;
                if (component != null)
                {
                    //save the Value of the Bodycontent
                  componentvalue = (String) component.getValue();
                }
                component = super.createVerbatimComponent();
                if (componentvalue != null)
                {
                    component.setValue(wrappedOutput + componentvalue);
                }
                else
                {
                    component.setValue(wrappedOutput);
                }
                wrappedResponse.reset();
            }
        }
        return component;
    }    
    
}
