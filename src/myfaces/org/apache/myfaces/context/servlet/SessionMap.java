/*
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
package net.sourceforge.myfaces.context.servlet;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * HttpSession attibutes as Map.
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SessionMap extends AbstractAttributeMap
{
    private final HttpServletRequest _httpRequest;
    private HttpSession _httpSession;

    SessionMap(HttpServletRequest httpRequest)
    {
        _httpRequest = httpRequest;
        _httpSession = httpRequest.getSession(false);
    }

    protected Object getAttribute(String key)
    {
        return _httpSession == null 
        ? null : _httpSession.getAttribute(key.toString());
    }

    protected void setAttribute(String key, Object value)
    {
        if (_httpSession == null)
        {
            _httpSession = _httpRequest.getSession(true);
        }

        _httpSession.setAttribute(key, value);
    }

    protected void removeAttribute(String key)
    {
        if (_httpSession != null)
        {
            _httpSession.removeAttribute(key);
        }
    }

    protected Enumeration getAttributeNames()
    {
        return _httpSession.getAttributeNames();
    }
}