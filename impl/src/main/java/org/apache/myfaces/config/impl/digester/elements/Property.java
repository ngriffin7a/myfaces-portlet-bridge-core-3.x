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
package org.apache.myfaces.config.impl.digester.elements;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 *
     The "property" element represents a JavaBean property of the Java class
     represented by our parent element.

     Property names must be unique within the scope of the Java class
     that is represented by the parent element, and must correspond to
     property names that will be recognized when performing introspection
     against that class via java.beans.Introspector.

    <!ELEMENT property        (description*, display-name*, icon*, property-name, property-class, default-value?, suggested-value?, property-extension*)>

 *          <p/>
 */
public class Property extends ElementBaseImpl
{
    private List _description;
    private List _displayName;
    private List _icon;
    private String _propertyName;
    private String _propertyClass;
    private String _defaultValue;
    private String _suggestedValue;
    private List _propertyExtension;


    public void addDescription(String value)
    {
        if(_description == null)
            _description = new ArrayList();

        _description.add(value);
    }

    public Iterator getDescriptions()
    {
        if(_description==null)
            return Collections.EMPTY_LIST.iterator();

        return _description.iterator();
    }

    public void addDisplayName(String value)
    {
        if(_displayName == null)
            _displayName = new ArrayList();

        _displayName.add(value);
    }

    public Iterator getDisplayNames()
    {
        if(_displayName==null)
            return Collections.EMPTY_LIST.iterator();

        return _displayName.iterator();
    }

    public void addIcon(String value)
    {
        if(_icon == null)
            _icon = new ArrayList();

        _icon.add(value);
    }

    public Iterator getIcons()
    {
        if(_icon==null)
            return Collections.EMPTY_LIST.iterator();

        return _icon.iterator();
    }

    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    public void setPropertyClass(String propertyClass)
    {
        _propertyClass = propertyClass;
    }

    public String getPropertyClass()
    {
        return _propertyClass;
    }

    public void setDefaultValue(String defaultValue)
    {
        _defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return _defaultValue;
    }

    public void setSuggestedValue(String suggestedValue)
    {
        _suggestedValue = suggestedValue;
    }

    public String getSuggestedValue()
    {
        return _suggestedValue;
    }

    public void addPropertyExtension(String propertyExtension)
    {
        if(_propertyExtension == null)
            _propertyExtension = new ArrayList();

        _propertyExtension.add(propertyExtension);
    }

    public Iterator getPropertyExtensions()
    {
        if(_propertyExtension==null)
            return Collections.EMPTY_LIST.iterator();

        return _propertyExtension.iterator();
    }

}
