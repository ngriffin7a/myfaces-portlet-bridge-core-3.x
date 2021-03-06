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

import java.io.Serializable;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentChildrenList
        extends AbstractList
        implements Serializable
{
    private static final long serialVersionUID = -6775078929331154224L;
    private UIComponent _component;
    private List _list = new ArrayList();
    private Map _idIndexedMap = new HashMap();

    _ComponentChildrenList(UIComponent component)
    {
        _component = component;
    }
    
    public UIComponent get(String id) {
       return (UIComponent) _idIndexedMap.get(id);
    }

    public Object get(int index)
    {
        return _list.get(index);
    }

    public int size()
    {
        return _list.size();
    }

    public Object set(int index, Object value)
    {
        checkValue(value);
        setNewParent((UIComponent)value);
        UIComponent child = (UIComponent) _list.set(index, value);
        resetParent(child);
        return child;
    }

    public boolean add(Object value)
    {
        checkValue(value);
        setNewParent((UIComponent)value);
        return _list.add(value);
    }

    public void add(int index, Object value)
    {
        checkValue(value);
        setNewParent((UIComponent)value);
        _list.add(index, value);
    }

    public Object remove(int index)
    {
        UIComponent child = (UIComponent) _list.remove(index);
        resetParent(child);
        return child;
    }


    private void setNewParent(UIComponent child)
    {
        UIComponent oldParent = child.getParent();
        if (oldParent != null)
        {
            oldParent.getChildren().remove(child);
        }
        child.setParent(_component);
        _idIndexedMap.put(child.getId(),child);
    }

    private void resetParent(UIComponent child) {
        if (child != null)
                child.setParent(null);
        _idIndexedMap.remove(child.getId());
    }

    private void checkValue(Object value)
    {
        if (value == null) throw new NullPointerException("value");
        if (!(value instanceof UIComponent)) throw new ClassCastException("value is not a UIComponent");
    }

    public void updateId(String oldId, UIComponent component) {
        _idIndexedMap.remove(oldId);
        _idIndexedMap.put(component.getId(), component);
    }
}
