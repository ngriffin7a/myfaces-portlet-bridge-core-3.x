/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.context;

import net.sourceforge.myfaces.renderkit.html.jspinfo.JspInfo;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesContextImpl
        extends FacesContext
{
    private ServletContext _servletcontext;
    private ServletRequest _servletrequest;
    private ServletResponse _servletresponse;
    private Lifecycle _lifecycle;
    private Locale _locale = Locale.getDefault();
    private Tree _requestTree = null;
    private Tree _responseTree = null;
    private List _applicationEvents = null;
    private List _messages = null;
    private List _messageComponents = null;
    private int _maximumSeverity = 0;
    private Map _requestEvents = null;
    private ResponseStream _responseStream = null;
    private ResponseWriter _responseWriter = null;

    public FacesContextImpl(ServletContext servletcontext,
                            ServletRequest servletrequest,
                            ServletResponse servletresponse,
                            Lifecycle lifecycle)
    {
        _servletcontext = servletcontext;
        _servletrequest = servletrequest;
        _servletresponse = servletresponse;
        _lifecycle = lifecycle;
    }

    //JSF.5.1.1
    public HttpSession getHttpSession()
    {
        if (_servletrequest instanceof HttpServletRequest)
        {
            return ((HttpServletRequest)_servletrequest).getSession();
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public HttpSession getHttpSession(boolean create)
    {
        if (_servletrequest instanceof HttpServletRequest)
        {
            return ((HttpServletRequest)_servletrequest).getSession(create);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public ServletContext getServletContext()
    {
        return _servletcontext;
    }

    public ServletRequest getServletRequest()
    {
        return _servletrequest;
    }

    public ServletResponse getServletResponse()
    {
        return _servletresponse;
    }

    //JSF.5.1.2
    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    //JSF.5.1.3
    public void setRequestTree(Tree tree)
    {
        if (_requestTree != null)
        {
            throw new IllegalStateException("Call release() first!");
        }
        _requestTree = tree;
    }

    public Tree getRequestTree()
    {
        return _requestTree;
    }

    public void setResponseTree(Tree tree)
    {
        _responseTree = tree;
    }

    public Tree getResponseTree()
    {
        return _responseTree != null ? _responseTree : _requestTree;
    }

    public void release()
    {
        _requestTree = null;
    }


    //JSF.5.1.4
    public Iterator getApplicationEvents()
    {
        return _applicationEvents != null
                ? _applicationEvents.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    public int getApplicationEventsCount()
    {
        return _applicationEvents != null
                ? _applicationEvents.size()
                : 0;
    }

    public void addApplicationEvent(FacesEvent facesevent)
    {
        if (_applicationEvents == null)
        {
            _applicationEvents = new ArrayList();
        }
        _applicationEvents.add(facesevent);
    }

    //JSF.5.1.5
    private static final Object NULL_DUMMY = new Object();
    public void addMessage(UIComponent uicomponent, Message message)
    {
        if (_messages == null)
        {
            _messages = new ArrayList();
            _messageComponents = new ArrayList();
        }
        _messages.add(message);
        _messageComponents.add(uicomponent != null ? uicomponent : NULL_DUMMY);
        if (message.getSeverity() > _maximumSeverity)
        {
            _maximumSeverity = message.getSeverity();
        }
    }

    public int getMaximumSeverity()
    {
        return _maximumSeverity;
    }

    public Iterator getMessages()
    {
        return _messages != null
                ? _messages.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    public Iterator getMessages(UIComponent uicomponent)
    {
        if (_messages == null)
        {
            return Collections.EMPTY_LIST.iterator();
        }
        List lst = new ArrayList();
        for (int i = 0; i < _messages.size(); i++)
        {
            UIComponent uic = (UIComponent)_messageComponents.get(i);
            if ((uicomponent == null && uic == NULL_DUMMY) ||
                uicomponent == uic)
            {
                lst.add(_messages.get(i));
            }
        }
        return lst.iterator();
    }


    //JSF.5.1.6
    public ApplicationHandler getApplicationHandler()
    {
        return _lifecycle.getApplicationHandler();
    }

    public ViewHandler getViewHandler()
    {
        return _lifecycle.getViewHandler();
    }


    //JSF.5.1.7
    public Class getModelType(String modelReference)
            throws FacesException
    {
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            return getModelClassFromJspInfo(modelReference);
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            if (obj == null)
            {
                //exception already thrown
                return null;
            }

            return BeanUtils.getBeanPropertyType(obj, propName);
        }
    }

    public Object getModelValue(String modelReference)
            throws FacesException
    {
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            return getModelInstance(modelReference);
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            if (obj == null)
            {
                //exception already thrown
                return null;
            }

            return BeanUtils.getBeanPropertyValue(obj, propName);
        }
    }

    public void setModelValue(String modelReference, Object value)
            throws FacesException
    {
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            setModelInstance(modelReference, value);
            return;
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            if (obj == null)
            {
                return;
            }

            BeanUtils.setBeanPropertyValue(obj, propName, value);
        }
    }


    //JSF.5.1.8
    public Iterator getRequestEvents(UIComponent uicomponent)
    {
        Collection c = getRequestEventsCollection(uicomponent);
        return c != null
                ? c.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    public int getRequestEventsCount(UIComponent uicomponent)
    {
        Collection c = getRequestEventsCollection(uicomponent);
        return c != null
                ? c.size()
                : 0;
    }

    private Collection getRequestEventsCollection(UIComponent uicomponent)
    {
        Collection c = null;
        if (_requestEvents != null)
        {
            c = (Collection)_requestEvents.get(uicomponent.getCompoundId());
        }
        return c;
    }

    public int getRequestEventsCount()
    {
        int count = 0;
        if (_requestEvents != null)
        {
            for (Iterator it = _requestEvents.values().iterator(); it.hasNext();)
            {
                Collection c = (Collection)it.next();
                count += c.size();
            }
        }
        return count;
    }

    public void addRequestEvent(UIComponent uicomponent, FacesEvent facesevent)
    {
        Collection c = null;
        if (_requestEvents == null)
        {
            _requestEvents = new HashMap();
        }
        else
        {
            c = getRequestEventsCollection(uicomponent);
        }

        if (c == null)
        {
            c = new ArrayList();
            _requestEvents.put(uicomponent.getCompoundId(), c);
        }

        c.add(facesevent);
    }


    //JSF.5.1.9

    public ResponseStream getResponseStream()
    {
        return _responseStream;
    }

    public ResponseWriter getResponseWriter()
    {
        return _responseWriter;
    }

    public void setResponseStream(ResponseStream responsestream)
    {
        _responseStream = responsestream;
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
        _responseWriter = responsewriter;
    }


    //Spezifische Erweiterungen:
    private Object getModelInstance(String modelId)
    {
        if (_servletcontext == null || _servletrequest == null)
        {
            throw new IllegalStateException("No servlet context or request!?");
        }
        Object obj;

        //Application context
        obj = _servletcontext.getAttribute(modelId);
        if (obj != null)
        {
            return obj;
        }

        //Session context
        if (_servletrequest instanceof HttpServletRequest)
        {
            HttpSession session = ((HttpServletRequest)_servletrequest).getSession(false);
            if (session != null)
            {
                obj = session.getAttribute(modelId);
                if (obj != null)
                {
                    return obj;
                }
            }
        }

        //Request context
        obj = _servletrequest.getAttribute(modelId);
        if (obj != null)
        {
            return obj;
        }

        //TODO: What about JSP PageContext?

        Class c = getModelClassFromJspInfo(modelId);
        if (c == null)
        {
            LogUtil.getLogger().severe("Could not determine class of model bean " + modelId);
            return null;
        }

        try
        {
            obj = c.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        _servletrequest.setAttribute(modelId, obj);

        return obj;
    }

    private void setModelInstance(String modelId, Object modelObj)
    {
        if (_servletcontext == null || _servletrequest == null)
        {
            throw new IllegalStateException("No servlet context or request!?");
        }
        Object obj;

        //Application context
        if (_servletcontext.getAttribute(modelId) != null)
        {
            _servletcontext.setAttribute(modelId, modelObj);
            return;
        }

        //Session context
        if (_servletrequest instanceof HttpServletRequest)
        {
            HttpSession session = ((HttpServletRequest)_servletrequest).getSession(false);
            if (session != null)
            {
                if (session.getAttribute(modelId) != null)
                {
                    session.setAttribute(modelId, modelObj);
                    return;
                }
            }
        }

        //TODO: What about JSP PageContext?

        //Request context (= default context)
        _servletrequest.setAttribute(modelId, modelObj);
    }


    /**
     * TODO: FacesContextImpl must not be specific to JSP rendering! So, we
     * should find a better way to find out model types.
     * @param modelId
     * @return
     * @throws FacesException
     */
    private Class getModelClassFromJspInfo(String modelId)
        throws FacesException
    {
        String className = null;

        className = JspInfo.getBeanType(this, getRequestTree().getTreeId(), modelId);
        if (className == null)
        {
            String responseTreeId = getResponseTree().getTreeId();
            if (!responseTreeId.equals(getRequestTree().getTreeId()))
            {
                className = JspInfo.getBeanType(this, responseTreeId, modelId);
            }
        }

        if (className == null)
        {
            return null;
        }

        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }
    }

}
