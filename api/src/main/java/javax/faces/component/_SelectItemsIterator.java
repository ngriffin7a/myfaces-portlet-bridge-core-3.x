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

import java.util.*;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;


/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _SelectItemsIterator implements Iterator
{
    private final Iterator _childs;
    private Iterator _nestedItems;    
    private Object _nextItem;
    private String _collectionLabel;
    private UISelectItems _currentUISelectItems;

    public _SelectItemsIterator(UIComponent selectItemsParent)
    {
        _childs = selectItemsParent.getChildren().iterator();
    }

    public boolean hasNext()
    {
        if(_nextItem != null)
        {
            return true;
        }
        if(_nestedItems != null)
        {
            if(_nestedItems.hasNext())
            {
                return true;
            }
            _nestedItems = null;
        }            
        if (_childs.hasNext())
        {
            UIComponent child = (UIComponent) _childs.next();
            // When there is other components nested that does
            // not extends from UISelectItem or UISelectItems
            // the behavior for this iterator is just skip this
            // element(s) until an element that extends from these
            // classes are found. If there is no more elements
            // that conform this condition, just return false.
            while (!(child instanceof UISelectItem)
                    && !(child instanceof UISelectItems))
            {
                //Try to skip it
                if (_childs.hasNext())
                {
                    //Skip and do the same check
                    child = (UIComponent) _childs.next();
                }
                else
                {
                    //End loop, so the final result is return false,
                    //since there are no more components to iterate.
                    return false;
                }
            }
            if (child instanceof UISelectItem)
            {
                UISelectItem uiSelectItem = (UISelectItem) child;
                Object item = uiSelectItem.getValue();
                if (item == null)
                {
                    Object itemValue = ((UISelectItem) child).getItemValue();
                    String label = ((UISelectItem) child).getItemLabel();
                    String description = ((UISelectItem) child)
                                    .getItemDescription();
                    boolean disabled = ((UISelectItem) child).isItemDisabled();
                    if (label == null)
                    {
                        label = itemValue.toString();
                    }
                    item = new SelectItem(itemValue, label, description,
                                    disabled);
                }
                else if (!(item instanceof SelectItem))
                {
                    ValueBinding binding = ((UISelectItem) child)
                                    .getValueBinding("value");
                    throw new IllegalArgumentException(
                                    "Value binding '"
                                    + (binding == null ? null : binding.getExpressionString())
                                    + "' of UISelectItem : "
                                    + getPathToComponent(child)
                                    + " does not reference an Object of type SelectItem");
                }
                _nextItem = item;
                return true;
            }
            else if (child instanceof UISelectItems)
            {
                _currentUISelectItems = ((UISelectItems) child);
                Object value = _currentUISelectItems.getValue();

                if (value instanceof SelectItem)
                {
                    _nextItem = value;
                    return true;
                }
                else if (value instanceof SelectItem[])
                {
                    _nestedItems = Arrays.asList((SelectItem[]) value)
                                    .iterator();
                    _collectionLabel = "Array";
                    return hasNext();
                }
                else if (value instanceof Collection)
                {
                    _nestedItems = ((Collection)value).iterator();
                    _collectionLabel = "Collection";
                    return hasNext();
                }
                else if (value instanceof Map)
                {
                    Map map = ((Map) value);
                    Collection items = new ArrayList(map.size()); 
                    for (Iterator it = map.entrySet().iterator(); it
                                    .hasNext();)
                    {
                        Map.Entry entry = (Map.Entry) it.next();
                        items.add(new SelectItem(entry.getValue(), entry
                                        .getKey().toString()));
                    }
                    _nestedItems = items.iterator();
                    _collectionLabel = "Map";
                    return hasNext();
                }
                else
                {
                    ValueBinding binding = _currentUISelectItems.getValueBinding("value");

                    throw new IllegalArgumentException(
                        "Value binding '"
                        + (binding == null ? null : binding
                                        .getExpressionString())
                        + "'of UISelectItems with component-path "
                        + getPathToComponent(child)
                        + " does not reference an Object of type SelectItem, SelectItem[], Collection or Map but of type : "
                        + ((value == null) ? null : value
                                        .getClass()
                                        .getName()));
                }
            }
        }
        return false;
    }

    public Object next()
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        if(_nextItem != null)
        {
            Object value = _nextItem;
            _nextItem = null;
            return value;
        }        
        if (_nestedItems != null)
        {
            Object item = _nestedItems.next();
            if (!(item instanceof SelectItem))
            {
                ValueBinding binding = _currentUISelectItems
                                .getValueBinding("value");
                throw new IllegalArgumentException(
                _collectionLabel + " referenced by UISelectItems with binding '"
                + (binding == null ? null : binding
                                        .getExpressionString())
                + "' and Component-Path : " + getPathToComponent(_currentUISelectItems)
                + " does not contain Objects of type SelectItem");
            }
            return item;
        }
        throw new NoSuchElementException();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }


    private String getPathToComponent(UIComponent component)
    {
        StringBuffer buf = new StringBuffer();

        if(component == null)
        {
            buf.append("{Component-Path : ");
            buf.append("[null]}");
            return buf.toString();
        }

        getPathToComponent(component,buf);

        buf.insert(0,"{Component-Path : ");
        buf.append("}");

        return buf.toString();
    }

    private void getPathToComponent(UIComponent component, StringBuffer buf)
    {
        if(component == null)
            return;

        StringBuffer intBuf = new StringBuffer();

        intBuf.append("[Class: ");
        intBuf.append(component.getClass().getName());
        if(component instanceof UIViewRoot)
        {
            intBuf.append(",ViewId: ");
            intBuf.append(((UIViewRoot) component).getViewId());
        }
        else
        {
            intBuf.append(",Id: ");
            intBuf.append(component.getId());
        }
        intBuf.append("]");

        buf.insert(0,intBuf.toString());

        if(component!=null)
        {
            getPathToComponent(component.getParent(),buf);
        }
    }
}
