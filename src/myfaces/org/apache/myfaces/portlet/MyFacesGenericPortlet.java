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
package org.apache.myfaces.portlet;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.config.FacesConfigurator;
import org.apache.myfaces.context.ReleaseableExternalContext;
import org.apache.myfaces.context.portlet.PortletExternalContextImpl;
import org.apache.myfaces.context.servlet.ServletFacesContextImpl;
import org.apache.myfaces.webapp.webxml.WebXml;

/**
 * This portlet initializes MyFaces and converts portlet requests into
 * JSF requests.
 *
 * @author  Stan Silvert (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2005/04/06 18:23:36  schof
 * Fixes MYFACES-101.  Used a patch from Stan while @infra sorts out his karma.
 *
 * Revision 1.2  2005/02/10 20:24:17  matzew
 * closed MYFACES-101 in Jira; Thanks to Stan Silvert (JBoss Group)
 *
 * Revision 1.1  2005/01/26 17:03:10  matzew
 * MYFACES-86. portlet support provided by Stan Silver (JBoss Group)
 *
 */
public class MyFacesGenericPortlet extends GenericPortlet 
{
    private static final Log log = LogFactory.getLog(MyFacesGenericPortlet.class);
    
    // PortletRequest parameter
    public static final String VIEW_ID =
        MyFacesGenericPortlet.class.getName() + ".VIEW_ID";
    
    // PortletSession attribute
    private static final String CURRENT_FACES_CONTEXT = 
        MyFacesGenericPortlet.class.getName() + ".CURRENT_FACES_CONTEXT";
    
    // portlet config parameter from portlet.xml
    private static final String DEFAULT_VIEW = "default-view";
    
    // portlet config parameter from portlet.xml
    private static final String DEFAULT_VIEW_SELECTOR = "default-view-selector";
    
    private static final String FACES_INIT_DONE =
        MyFacesGenericPortlet.class.getName() + ".FACES_INIT_DONE";
    
    private PortletContext portletContext;
    
    private FacesContextFactory facesContextFactory;
    private Lifecycle lifecycle;
    
    private String defaultView;
    private DefaultViewSelector defaultViewSelector;
    
    /** 
     * Creates a new instance of MyFacesPortlet 
     */
    public MyFacesGenericPortlet() 
    {
    }
    
    /**
     * Portlet lifecycle.
     */
    public void destroy() 
    {
        super.destroy();
        FactoryFinder.releaseFactories();
    }
    
    /**
     * Portlet lifecycle.
     */
    public void init() throws PortletException, UnavailableException 
    {
        this.portletContext = getPortletContext();
        setDefaultView();
        setDefaultViewSelector();
        initMyFaces();
        
        facesContextFactory = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

        // Javadoc says: Lifecycle instance is shared across multiple simultaneous requests, it must be 
        // implemented in a thread-safe manner.  So we can acquire it here once:
        LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
    }
    
    private void setDefaultView() throws UnavailableException
    {
        this.defaultView = getPortletConfig().getInitParameter(DEFAULT_VIEW);
        if (defaultView == null)
        {
            String msg = "Fatal: must specify a JSF view id as the default view in portlet.xml";
            throw new UnavailableException(msg);
        }
    }
    
    private void setDefaultViewSelector() throws UnavailableException
    {
        String selectorClass = getPortletConfig().getInitParameter(DEFAULT_VIEW_SELECTOR);
        if (selectorClass == null) return;
        
        try 
        {
            this.defaultViewSelector = (DefaultViewSelector)Class.forName(selectorClass).newInstance();
            this.defaultViewSelector.setPortletContext(getPortletContext());
        }
        catch (Exception e)
        {
            log.error("Failed to load " + DEFAULT_VIEW_SELECTOR, e);
            throw new UnavailableException(e.getMessage());
        }
    }
    
    private String getLifecycleId() 
    {
        String lifecycleId = getPortletConfig().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }
    
    private void initMyFaces() 
    {
        try 
        {
            Boolean b = (Boolean)portletContext.getAttribute(FACES_INIT_DONE);

            if (b == null || b.booleanValue() == false) 
            {
                log.trace("Initializing MyFaces");

                //Load the configuration
                ExternalContext externalContext = new PortletExternalContextImpl(portletContext, null, null);

                //And configure everything
                new FacesConfigurator(externalContext).configure();

                // parse web.xml - not sure if this is needed for portlet
                WebXml.init(externalContext);

                portletContext.setAttribute(FACES_INIT_DONE, Boolean.TRUE);
            }
            else 
            {
                log.info("MyFaces already initialized");
            }
        } 
        catch (Exception ex) 
        {
            log.error("Error initializing MyFacesGenericPortlet", ex);
        }
        
        log.info("PortletContext '" + portletContext.getRealPath("/") + "' initialized.");
    }
    
    /**
     * Called by the portlet container to allow the portlet to process an action request.
     */
    public void processAction(ActionRequest request, ActionResponse response) 
            throws PortletException, IOException 
    {
        if (log.isTraceEnabled()) log.trace("called processAction");
        
        if (sessionTimedOut(request)) return;
        
        setPortletRequestFlag(request);
        
        FacesContext facesContext = facesContext(request, response);
                
        try
        {
            lifecycle.execute(facesContext);
            
            if (!facesContext.getResponseComplete())
            {
                response.setRenderParameter(VIEW_ID, facesContext.getViewRoot().getViewId());
            }
            
            request.getPortletSession().setAttribute(CURRENT_FACES_CONTEXT, facesContext);
        }
        catch (Throwable e)
        {
            facesContext.release();
            handleExceptionFromLifecycle(e);
        }
    }

    private void handleExceptionFromLifecycle(Throwable e) 
            throws PortletException, IOException 
    {
        logException(e, null);

        if (e instanceof IOException) 
        {
            throw (IOException)e;
        }
        
        if (e instanceof PortletException) 
        {
            throw (PortletException)e;
        }
        
        if (e.getMessage() != null) 
        {
            throw new PortletException(e.getMessage(), e);
        }
        
        throw new PortletException(e);
    }
    
    /**
     * Helper method to serve up the view mode.
     */
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException 
    {
        facesRender(request, response);
    }
    
    /**
     * Helper method to serve up the edit mode.  Can be overridden to add
     * the edit mode concept to a JSF application.
     */
    protected void doEdit(RenderRequest request, RenderResponse response)
            throws PortletException, IOException 
    {
        facesRender(request, response);
    }
    
    /**
     * Helper method to serve up the edit mode.  Can be overridden to add
     * the help mode concept to a JSF application.
     */
    protected void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException 
    {
        facesRender(request, response);
    }
    
    // JSF Spec section 2.1.1
    protected void nonFacesRequest(RenderRequest request, RenderResponse response) throws PortletException
    {
        if (log.isTraceEnabled()) log.trace("Non-faces request: contextPath = " + request.getContextPath());
        ApplicationFactory appFactory = 
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = appFactory.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        FacesContext facesContext = facesContext(request, response);
        UIViewRoot view = viewHandler.createView(facesContext, selectDefaultView(request, response));
        facesContext.setViewRoot(view);
        lifecycle.render(facesContext);
    }
    
    private String selectDefaultView(RenderRequest request, RenderResponse response) throws PortletException
    {
        String view = this.defaultView;
        if (this.defaultViewSelector != null)
        {
            String selectedView = this.defaultViewSelector.selectViewId(request, response);
            if (selectedView != null)
            {
                view = selectedView;
            }
        }
            
        return view;
    }
    
    private FacesContext facesContext(PortletRequest request, 
                                      PortletResponse response)
    {
        return facesContextFactory.getFacesContext(portletContext,
                                                   request,
                                                   response,
                                                   lifecycle);
    }
    
    private ReleaseableExternalContext makeExternalContext(PortletRequest request,
                                                           PortletResponse response)
    {
        return (ReleaseableExternalContext)new PortletExternalContextImpl(portletContext, request, response);
    }
    
    private boolean sessionTimedOut(PortletRequest request) 
    {
        return request.getPortletSession(false) == null;
    }
    
    private void setPortletRequestFlag(PortletRequest request)
    {
        request.getPortletSession().setAttribute(PortletUtil.PORTLET_REQUEST_FLAG, "true");
    }
    
    /**
     * Render a JSF view.
     */
    protected void facesRender(RenderRequest request, RenderResponse response)
            throws PortletException, java.io.IOException 
    {
        if (log.isTraceEnabled()) log.trace("called facesRender");

        response.setContentType("text/html");
        
        String viewId = request.getParameter(VIEW_ID);
        if ((viewId == null) || sessionTimedOut(request))
        {
            setPortletRequestFlag(request);
            nonFacesRequest(request,  response);
            return;
        }
       
        setPortletRequestFlag(request);
        
        try
        {
            ServletFacesContextImpl facesContext = (ServletFacesContextImpl)request.
                                                   getPortletSession().
                                                   getAttribute(CURRENT_FACES_CONTEXT);
            
            // TODO: not sure if this can happen.  Also double check this against spec section 2.1.3
            if (facesContext.getResponseComplete()) return;
            
            facesContext.setExternalContext(makeExternalContext(request, response));
            lifecycle.render(facesContext);
        }
        catch (Throwable e)
        {
            handleExceptionFromLifecycle(e);
        }
    }
    
    private void logException(Throwable e, String msgPrefix) {
        String msg;
        if (msgPrefix == null) 
        {
            if (e.getMessage() == null) 
            {
                msg = "Exception in FacesServlet";
            } 
            else 
            {
                msg = e.getMessage();
            }
        } 
        else 
        {
            if (e.getMessage() == null) 
            {
                msg = msgPrefix;
            }
            else 
            {
                msg = msgPrefix + ": " + e.getMessage();
            }
        }

        portletContext.log(msg, e);

        Throwable cause = e.getCause();
        if (cause != null && cause != e)
        {
            logException(cause, "Root cause");
        }

        if(e instanceof PortletException)
        {
            cause = ((PortletException) e).getCause();

            if(cause != null && cause != e)
            {
                logException(cause, "Root cause of PortletException");
            }
        }
    }
    
}