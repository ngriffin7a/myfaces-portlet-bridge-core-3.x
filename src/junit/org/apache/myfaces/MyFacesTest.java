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
package net.sourceforge.myfaces;

import junit.framework.TestCase;
import net.sourceforge.myfaces.application.ApplicationFactoryImpl;
import net.sourceforge.myfaces.context.ServletContextMockImpl;
import net.sourceforge.myfaces.context.ServletRequestMockImpl;
import net.sourceforge.myfaces.context.ServletResponseMockImpl;
import net.sourceforge.myfaces.context.servlet.ServletFacesContextImpl;
import net.sourceforge.myfaces.lifecycle.LifecycleImpl;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MyFacesTest
    extends TestCase
{
    protected Application _application;
    protected ServletContext _servletContext;
    protected HttpServletRequest _httpServletRequest;
    protected HttpServletResponse _httpServletResponse;
    protected Lifecycle _lifecycle;
    protected FacesContext _facesContext;


    public MyFacesTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, 
            ApplicationFactoryImpl.class.getName());
        
//        FIXME
//        LifecycleFactory lf = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
//        _lifecycle = lf.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        _lifecycle = new LifecycleImpl();
        
        _servletContext = new ServletContextMockImpl();
        _httpServletRequest = new ServletRequestMockImpl(getCookies());
        _httpServletResponse = new ServletResponseMockImpl();

        _facesContext = new ServletFacesContextImpl(
            _servletContext, _httpServletRequest, _httpServletResponse);

        _application = _facesContext.getApplication();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        _application = null;
        _servletContext = null;
        _httpServletRequest = null;
        _httpServletResponse = null;
        _lifecycle = null;
        _facesContext = null;
    }

    /** override where necessary */
    protected Cookie[] getCookies()
    {
        return new Cookie[0];
    }
}
