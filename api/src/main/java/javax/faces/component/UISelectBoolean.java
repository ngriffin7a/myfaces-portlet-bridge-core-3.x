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
package javax.faces.component;

import javax.faces.el.ValueBinding;


/**
 * A component that allows the user to select or unselect an object.
 * <p>
 * This can also be used to choose between two states such as true/false or on/off.
 * <p>
 * See the javadoc for this class in the
 * <a href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/api/index.html">JSF Specification</a>
 * for further details.
 *
 * @JSFComponent
 *   type = "javax.faces.SelectBoolean"
 *   family = "javax.faces.SelectBoolean"
 *   desc = "UISelectBoolean"
 *   
 * @JSFJspProperty name = "value" returnType = "java.lang.Boolean"
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISelectBoolean extends UIInput
{
    public static final String COMPONENT_TYPE = "javax.faces.SelectBoolean";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectBoolean";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Checkbox";

    public UISelectBoolean()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public boolean isSelected()
    {
        Boolean value = (Boolean)getSubmittedValue();
        if( value == null )
            value = (Boolean)getValue();

        return value != null && value.booleanValue();
    }

    public void setSelected(boolean selected)
    {
        setValue(Boolean.valueOf(selected));
    }

    public ValueBinding getValueBinding(String name)
    {
        if (name == null) throw new NullPointerException("name");
        if (name.equals("selected"))
        {
            return super.getValueBinding("value");
        }
        else
        {
            return super.getValueBinding(name);
        }
    }

    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (name == null) throw new NullPointerException("name");
        if (name.equals("selected"))
        {
            super.setValueBinding("value", binding);
        }
        else
        {
            super.setValueBinding(name, binding);
        }
    }
}
