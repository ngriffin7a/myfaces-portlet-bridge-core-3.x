/**
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

package javax.faces.model;

import java.util.List;

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class ListDataModel extends DataModel
{

    // FIELDS
    private int _rowIndex = -1;
    private List _data;

    // CONSTRUCTORS
    public ListDataModel()
    {
        super();
    }

    public ListDataModel(Object[] list)
    {
        if (list == null) throw new NullPointerException("list");
        setWrappedData(list);
        throw new UnsupportedOperationException();
    }

    // METHODS
    public int getRowCount()
    {
        if (_data == null)
        {
            return -1;
        }
        return _data.size();
    }

    public Object getRowData()
    {
        if (_data == null)
        {
            return null;
        }
        if (!isRowAvailable())
        {
            throw new IllegalArgumentException("row is unavailable");
        }
        return _data.get(_rowIndex);
    }

    public int getRowIndex()
    {
        return _rowIndex;
    }

    public Object getWrappedData()
    {
        return _data;
    }

    public boolean isRowAvailable()
    {
        if (_data == null)
        {
            return false;
        }
        return _rowIndex >= 0 && _rowIndex < _data.size();
    }

    public void setRowIndex(int rowIndex)
    {
        if (rowIndex < -1)
        {
            throw new IllegalArgumentException("illegal rowIndex " + rowIndex);
        }
        int oldRowIndex = _rowIndex;
        _rowIndex = rowIndex;
        if (_data != null && oldRowIndex != _rowIndex)
        {
            Object data = isRowAvailable() ? getRowData() : null;
            DataModelEvent event = new DataModelEvent(this, _rowIndex, data);
            DataModelListener[] listeners = getDataModelListeners();
            for (int i = 0; i < listeners.length; i++)
            {
                listeners[i].rowSelected(event);
            }
        }
    }

    public void setWrappedData(Object data)
    {
        _data = (List)data;
        int rowIndex = _data != null ? 0 : -1;
        setRowIndex(rowIndex);
    }

}
