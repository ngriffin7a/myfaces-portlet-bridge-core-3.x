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
package net.sourceforge.myfaces.custom.panelstack;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import net.sourceforge.myfaces.custom.tabbedpane.TabChangeListener;
import net.sourceforge.myfaces.custom.tabbedpane.TabChangeEvent;

/**
 * Manage a stack of JSF components and allow for one child component to be choosen for rendering. The behaviour
 * is similar to the CardLayout of Java Swing. Property <code>selectedPanel</code> defines the id of the child
 * to be rendered. If no child panel is selected or if the selected panel can not be found the first child is rendered.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/08/15 22:42:11  o_rossmueller
 *          new custom component: HtmlPanelStack
 *
 *
 */
public class HtmlPanelStack extends HtmlPanelGroup
{

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlPanelStack";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.PanelStack";

    private String selectedPanel = null;

    public HtmlPanelStack()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSelectedPanel(String selectedPanel)
    {
        this.selectedPanel = selectedPanel;
    }

    public String getSelectedPanel()
    {
        if (selectedPanel != null) return selectedPanel;
        ValueBinding vb = getValueBinding("selectedPanel");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = selectedPanel;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        selectedPanel = (String)values[1];
    }
}
