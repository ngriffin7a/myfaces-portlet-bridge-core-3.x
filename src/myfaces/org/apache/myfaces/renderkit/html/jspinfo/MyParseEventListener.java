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
package net.sourceforge.myfaces.renderkit.html.jspinfo;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.ext.UISaveState;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.Constants;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JasperException;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.JspCompilationContext;
import net.sourceforge.myfaces.renderkit.html.jspinfo.jasper.compiler.*;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;
import net.sourceforge.myfaces.taglib.MyFacesTag;
import net.sourceforge.myfaces.taglib.UIComponentTagHacks;
import net.sourceforge.myfaces.taglib.core.ActionListenerTag;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import org.xml.sax.Attributes;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import javax.faces.webapp.FacetTag;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyParseEventListener
        implements ParseEventListener
{
    private TagLibraries _tagLibraries = new MyTagLibraries();
    private JspTreeParser _parser;
    private JspCompilationContext _ctxt;
    private UIComponent _currentComponent;
    private JspInfo _jspInfo;

    private static final String AUTO_ID_PREFIX = "autoId";
    private int _autoId;

    public MyParseEventListener(JspTreeParser parser,
                                JspCompilationContext ctxt,
                                JspInfo jspInfo)
    {
        _parser = parser;
        _ctxt = ctxt;
        //_currentComponent = jspInfo.getTree().getRoot();
        _currentComponent = null;
        _jspInfo = jspInfo;
    }

    public void beginPageProcessing() throws JasperException
    {
        //System.out.println("MyParseEventListener.beginPageProcessing");
    }

    public void endPageProcessing() throws JasperException
    {
        //System.out.println("MyParseEventListener.endPageProcessing");
    }

    /*
     * Custom tag support
     */
    public TagLibraries getTagLibraries()
    {
        //System.out.println("MyParseEventListener.getTagLibraries");
        return _tagLibraries;
    }

    public void handleBean(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        handleBean(attrs);
    }

    public void handleBean(Mark start, Mark stop, Attributes attrs, boolean isXml)
            throws JasperException
    {
        handleBean(attrs);
    }

    public void handleBeanEnd(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleBeanEnd");
    }

    public void handleCharData(Mark start, Mark stop, char[] chars)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleCharData");
    }

    public void handleComment(Mark start, Mark stop, char[] text) throws JasperException
    {
        //System.out.println("MyParseEventListener.handleComment");
    }

    public void handleDeclaration(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleDeclaration");
    }

    public void handleDirective(String directive,
                                Mark start, Mark stop,
                                Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleDirective");

        if (directive.equals("taglib"))
        {
            String uri = attrs.getValue("uri");
            String prefix = attrs.getValue("prefix");

            TagLibraryInfo tl = null;

            String[] location = _ctxt.getTldLocation(uri);
            if (location == null)
            {
                tl = new TagLibraryInfoImpl(_ctxt, prefix, uri);
            }
            else
            {
                tl = new TagLibraryInfoImpl(_ctxt, prefix, uri, location);
            }
            _tagLibraries.addTagLibrary(prefix, tl);
        }

        else if (directive.equals("include"))
        {
            String file = attrs.getValue("file");
            if (file == null)
            {
                throw new CompileException(start,
                                           Constants.getString("jsp.error.include.missing.file"));
            }

            _parser.parseIncludeFile(file);
        }

    }

    public void handleExpression(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleExpression");
    }

    public void handleForward(Mark start, Mark stop, Attributes attrs, Hashtable param)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleForward");
    }

    public void handleForward(Mark start, Mark stop, Attributes attrs, Hashtable param, boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleForward");
    }

    public void handleGetProperty(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleGetProperty");
    }

    public void handleInclude(Mark start, Mark stop, Attributes attrs, Hashtable param)
            throws JasperException
    {
        System.out.println("MyParseEventListener.handleInclude");
    }

    public void handleInclude(Mark start, Mark stop, Attributes attrs, Hashtable param, boolean isXml)
            throws JasperException
    {
        System.out.println("MyParseEventListener.handleInclude");
    }

    public void handleJspCdata(Mark start, Mark stop, char[] data)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleJspCdata");
    }

    public void handlePlugin(Mark start, Mark stop, Attributes attrs, Hashtable param,
                             String fallback)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handlePlugin");
    }

    public void handlePlugin(Mark start, Mark stop, Attributes attrs, Hashtable param,
                             String fallback, boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handlePlugin");
    }

    public void handleRootBegin(Attributes attrs) throws JasperException
    {
        //System.out.println("MyParseEventListener.handleRootBegin");
    }

    public void handleRootEnd()
    {
        //System.out.println("MyParseEventListener.handleRootEnd");
    }

    public void handleScriptlet(Mark start, Mark stop, Attributes attrs, char[] text)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleScriptlet");
    }

    public void handleSetProperty(Mark start, Mark stop, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleSetProperty");
    }

    public void handleSetProperty(Mark start, Mark stop, Attributes attrs,
                                  boolean isXml)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleSetProperty");
    }

    /*
     * start: is either the start position at "<" if content type is JSP or empty, or
     *        is the start of the body after the "/>" if content type is tag dependent
     * stop: can be null if the body contained JSP tags...
     */
    public void handleTagBegin(Mark start, Mark stop, Attributes attrs, String prefix, String shortTagName,
                               TagLibraryInfo tli, TagInfo ti, boolean hasBody)
            throws JasperException
    {
        handleTagBegin(prefix, shortTagName, attrs, tli, ti, start.getFile(), start.getLineNumber(), stop.getLineNumber());
    }

    public void handleTagBegin(Mark start, Mark stop, Attributes attrs, String prefix, String shortTagName,
                               TagLibraryInfo tli, TagInfo ti, boolean hasBody, boolean isXml)
            throws JasperException
    {
        handleTagBegin(prefix, shortTagName, attrs, tli, ti, start.getFile(), start.getLineNumber(), stop.getLineNumber());
    }

    public void handleTagEnd(Mark start, Mark stop, String prefix, String shortTagName,
                             Attributes attrs, TagLibraryInfo tli, TagInfo ti, boolean hasBody)
            throws JasperException
    {
        handleTagEnd(prefix, shortTagName, attrs, tli, ti);
    }

    public void handleUninterpretedTagBegin(Mark start, Mark stop,
                                            String rawName, Attributes attrs)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleUninterpretedTagBegin");
    }

    public void handleUninterpretedTagEnd(Mark start, Mark stop,
                                          String rawName, char[] data)
            throws JasperException
    {
        //System.out.println("MyParseEventListener.handleUninterpretedTagEnd");
    }

    public void setDefault(boolean b)
    {
        //System.out.println("MyParseEventListener.setDefault");
    }

    public void setReader(JspReader reader)
    {
    }

    public void setTemplateInfo(Mark start, Mark stop)
    {
        //System.out.println("MyParseEventListener.setTemplateInfo");
    }



    private HashMap _tagInstances = new HashMap();
    private static final Object NULL_DUMMY = new Object();
    private Tag getTagInstance(TagInfo ti)
    {
        Object obj = _tagInstances.get(ti.getTagClassName());
        if (obj == NULL_DUMMY)
        {
            return null;
        }
        else if (obj != null)
        {
            return (Tag)obj;
        }

        Class c;
        try
        {
            c = Class.forName(ti.getTagClassName());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class for tag " + ti.getTagName() + " not found!", e);
        }

        if (UIComponentTag.class.isAssignableFrom(c) ||
            ActionListenerTag.class.isAssignableFrom(c) ||
            ValidatorTag.class.isAssignableFrom(c) ||
            FacetTag.class.isAssignableFrom(c))
        {
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

            _tagInstances.put(ti.getTagClassName(), obj);
            return (Tag)obj;
        }
        else
        {
            _tagInstances.put(ti.getTagClassName(), NULL_DUMMY);
            return null;
        }
    }


    private void handleTagBegin(String prefix, String shortTagName,
                                Attributes attrs, TagLibraryInfo tli, TagInfo ti,
                                String filename, int startLine, int endLine)
    {
        Tag tag = getTagInstance(ti);
        if (tag == null)
        {
            return;
        }
        else if (tag instanceof UIComponentTag)
        {
            handleUIComponentTag(ti, (UIComponentTag)tag, attrs, filename, startLine, endLine);
        }
        else if (tag instanceof ActionListenerTag)
        {
            handleActionListenerTag(ti, (ActionListenerTag)tag, attrs);
        }
        else if (tag instanceof ValidatorTag)
        {
            handleValidatorTag(ti, (ValidatorTag)tag, attrs);
        }
        else if (tag instanceof FacetTag)
        {
            handleFacetTag(attrs);
        }
    }


    private void handleUIComponentTag(TagInfo ti,
                                      UIComponentTag facesTag,
                                      Attributes attrs,
                                      String filename, int startLine, int endLine)
    {
        /*
        if (isRootUIComponentTag(facesTag))
        {
            //This is the UseFacesTag which represents the root component
            if (_currentComponent != null)
            {
                throw new IllegalStateException("Current component already set?");
            }
            _currentComponent = _jspInfo.getTree().getRoot();
            return;
        }
        */
        if (_currentComponent == null)
        {
            //This must be the UseFacesTag, which represents the root component
            _currentComponent = _jspInfo.getTree().getRoot();
            return;
        }

        String id = null;

        BeanInfo beanInfo = BeanUtils.getBeanInfo(facesTag);

        TagAttributeInfo[] attrInfos = ti.getAttributes();
        for (int i = 0; i < attrInfos.length; i++)
        {
            TagAttributeInfo attrInfo = attrInfos[i];
            String attrName = attrInfo.getName();
            Object attrValue = attrs.getValue(attrName);

            if (attrValue != null)
            {
                String strValue = ((String)attrValue).trim();
                if (attrInfo.canBeRequestTime() &&
                    (strValue.startsWith("<%") || strValue.startsWith("{")))
                {
                    //Request time value --> ignore
                    continue;
                }

                if (attrName.equals(CommonRendererAttributes.ID_ATTR))
                {
                    id = (String)attrValue;
                }
                else
                {
                    PropertyDescriptor propDescr = BeanUtils.findPropertyDescriptor(beanInfo, attrName);
                    if (propDescr == null)
                    {
                        throw new RuntimeException("No PropertyDescriptor found for tag property " + attrName);
                    }

                    if (attrValue instanceof String)
                    {
                        if (attrInfo.getTypeName() != null)
                        {
                            Class type = null;
                            try
                            {
                                type = Class.forName(attrInfo.getTypeName());
                            }
                            catch (ClassNotFoundException e)
                            {
                                throw new RuntimeException(e);
                            }
                            attrValue = convertStringToTargetType((String)attrValue, type);
                        }
                        else
                        {
                            attrValue = convertStringToTargetType(propDescr,
                                                                  (String)attrValue);
                        }
                    }
                    BeanUtils.setBeanPropertyValue(facesTag, propDescr, attrValue);
                }
            }
        }

        String componentType = UIComponentTagHacks.getComponentType(facesTag);
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        UIComponent comp = af.getApplication().getComponent(componentType);
        /*
        if (comp == null)
        {
            LogUtil.getLogger().warning("Tag class " + facesTag.getClass().getName() + " did not create a component.");
            //We set current component to a dummy, so that the
            //getParent in handleEndTag returns the right component:
            final UIComponent currComp = _currentComponent;
            _currentComponent = new UIComponentBase()
            {
                public String getComponentType()
                {
                    return "DUMMY";
                }

                public UIComponent getParent()
                {
                    return currComp;
                }
            };
            facesTag.release();
            return;
        }
        */

        if (id != null)
        {
            comp.setComponentId(id);
            comp.setAttribute(JspInfo.HARDCODED_ID_ATTR, id);
        }
        else
        {
            if (!(facesTag instanceof MyFacesTag ||
                  facesTag instanceof MyFacesBodyTag))
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                int mode = MyFacesConfig.getStateSavingMode(((ServletContext)facesContext.getExternalContext().getContext()));
                if (mode == MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED ||
                    mode == MyFacesConfig.STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED)
                {
                    //Only subclasses of MyFacesTag or MyFacesBodyTag can make
                    //sure that each component without an id is assigned the
                    //proper auto-id from the static tree.
                    throw new FacesException("Tag handler for '" + ti.getTagName() + "' is no subclass of MyFacesTag and has no id. When using state saving mode \"client_minimized\" or \"client_minimized_zipped\" you must assign an id to each non-MyFacesTag!");
                }
            }
            String newAutoId = AUTO_ID_PREFIX + (++_autoId);
            comp.setComponentId(newAutoId);
            LogUtil.getLogger().finest("Tag '" + ti.getTagName() + "' has no id, assigning auto id '" + newAutoId + "' to component.");
        }

        String rendererType = facesTag.getRendererType();
        if (rendererType != null)
        {
            comp.setRendererType(rendererType);
        }

        String facetName = (String)_currentComponent.getAttribute(PENDING_FACET_ATTR);
        if (facetName != null)
        {
            UIComponentUtils.addFacet(_currentComponent, facetName, comp);
            _currentComponent.setAttribute(PENDING_FACET_ATTR, null);
        }
        else
        {
            _currentComponent.addChild(comp);
        }


        UIComponentTagHacks.setCreated(facesTag, true);
        UIComponentTagHacks.overrideProperties(facesTag, comp);
        facesTag.release(); //Just to be sure

        _currentComponent = comp;

        //Remember the tag class that created this component
        comp.setAttribute(JspInfo.CREATOR_TAG_CLASS_ATTR, facesTag.getClass().getName());

        //Remember JSP line number
        comp.setAttribute(JspInfo.JSP_POSITION_ATTR,
                          new Object[] {filename,
                                        new Integer(startLine),
                                        new Integer(endLine)});

        if (comp instanceof UISaveState)
        {
            _jspInfo.addUISaveStateComponent(comp);
        }

        //We can be sure, that each parent has a valid componentId, so
        //getClientId should not have a side-effect
        _jspInfo.getComponentMap().put(getClientId(comp), comp);
    }


    /*
    protected boolean isRootUIComponentTag(UIComponentTag tag)
    {
        Tag find = tag.getParent();
        while (find != null)
        {
            if (find instanceof UIComponentTag)
            {
                return false;
            }
            find = find.getParent();
        }
        return true;
    }
    */


    private String getClientId(UIComponent comp)
    {
        UIComponent findContainerComp = comp.getParent();
        while (findContainerComp != null && !(findContainerComp instanceof NamingContainer))
        {
            findContainerComp = findContainerComp.getParent();
        }
        if (findContainerComp == null)
        {
            throw new IllegalArgumentException("Root is no naming container?");
        }

        String componentId = comp.getComponentId();
        if (componentId == null)
        {
            throw new IllegalStateException("Component has no id?!");
        }
        if (findContainerComp.getParent() == null)
        {
            //container is root
            return componentId;
        }
        else
        {
            return getClientId(findContainerComp) + UIComponent.SEPARATOR_CHAR + componentId;
        }
    }




    private Object convertStringToTargetType(PropertyDescriptor propertyDescriptor,
                                             String propertyStringValue)
    {
        Class propertyEditorClass = propertyDescriptor.getPropertyEditorClass();
        if (propertyEditorClass != null)
        {
            PropertyEditor pe = null;
            try
            {
                pe = (PropertyEditor)propertyEditorClass.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            pe.setAsText(propertyStringValue);
            return pe.getValue();
        }

        return convertStringToTargetType(propertyStringValue,
                                         propertyDescriptor.getPropertyType());
    }


    private Object convertStringToTargetType(String s,
                                             Class targetClass)
    {
        if (String.class.isAssignableFrom(targetClass))
        {
            //already a String
            return s;
        }
        else if (targetClass.equals(Boolean.TYPE) ||
                 targetClass.equals(Boolean.class))
        {
            return Boolean.valueOf(s);
        }
        else if (targetClass.equals(Byte.TYPE) ||
                 targetClass.equals(Byte.class))
        {
            return Byte.valueOf(s);
        }
        else if (targetClass.equals(Character.TYPE) ||
                 targetClass.equals(Character.class))
        {
            return s.length() > 0 ? new Character(s.charAt(0)) : null;
        }
        else if (targetClass.equals(Double.TYPE) ||
                 targetClass.equals(Double.class))
        {
            return Double.valueOf(s);
        }
        else if (targetClass.equals(Integer.TYPE) ||
                 targetClass.equals(Integer.class))
        {
            return Integer.valueOf(s);
        }
        else if (targetClass.equals(Float.TYPE) ||
                 targetClass.equals(Float.class))
        {
            return Float.valueOf(s);
        }
        else if (targetClass.equals(Long.TYPE) ||
                 targetClass.equals(Long.class))
        {
            return Long.valueOf(s);
        }
        else if (targetClass.equals(Short.TYPE) ||
                 targetClass.equals(Short.class))
        {
            return Short.valueOf(s);
        }
        else if (targetClass.getName().equals("java.lang.Object"))
        {
            return s;
        }
        else
        {
            LogUtil.getLogger().severe("Could not convert String '" + s + "' to target type " + targetClass.getName());
            return s;
        }
    }


    private void handleTagEnd(String prefix, String shortTagName,
                              Attributes attrs, TagLibraryInfo tli, TagInfo ti)
    {
        Tag tag = getTagInstance(ti);
        if (tag != null &&
            tag instanceof UIComponentTag)
        {
            _currentComponent = _currentComponent.getParent();
        }
    }

    private void handleBean(Attributes attrs)
    {
        String id = attrs.getValue("id");
        if (id == null)
        {
            throw new IllegalArgumentException("No id attribute!");
        }

        String className = attrs.getValue("class");
        String beanName = attrs.getValue("beanName");

        int scope = PageContext.PAGE_SCOPE; //Default as stated in JSP Spec.
        String scopeValue = attrs.getValue("scope");
        if (scopeValue != null)
        {
            scopeValue = scopeValue.trim();
            if (scopeValue.equalsIgnoreCase("page"))
            {
                scope = PageContext.PAGE_SCOPE;
            }
            else if (scopeValue.equalsIgnoreCase("request"))
            {
                scope = PageContext.REQUEST_SCOPE;
            }
            else if (scopeValue.equalsIgnoreCase("session"))
            {
                scope = PageContext.SESSION_SCOPE;
            }
            else if (scopeValue.equalsIgnoreCase("application"))
            {
                scope = PageContext.APPLICATION_SCOPE;
            }
            else
            {
                LogUtil.getLogger().warning("Illegal scope attribute '" + scopeValue + "'. Assuming page scope.");
            }
        }

        //We ignore the scope for convenience, although at the moment it is not
        //necessary to store the type of application or session scope beans.
        _jspInfo.setJspBeanInfo(id, new JspBeanInfo(id,
                                                    className,
                                                    beanName,
                                                    scope));
    }


    private static final String ACTION_LISTENER_TAG_TYPE_ATTR = "type";

    private void handleActionListenerTag(TagInfo ti,
                                         ActionListenerTag actionListenerTag,
                                         Attributes attrs)
    {
        String type = attrs.getValue(ACTION_LISTENER_TAG_TYPE_ATTR);
        if (type == null)
        {
            LogUtil.getLogger().severe("action_listener tag has no " + ACTION_LISTENER_TAG_TYPE_ATTR + " attribute!");
            return;
        }

        if (!(_currentComponent instanceof UICommand))
        {
            LogUtil.getLogger().severe("Cannot register action listener because component " + _currentComponent + " is no UICommand.");
            return;
        }

        ActionListener actionListener;
        try
        {
            Class clazz = Class.forName(type);
            actionListener = (ActionListener)clazz.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        ActionListener wrapped = new StaticActionListenerWrapper(actionListener);
        ((UICommand)_currentComponent).addActionListener(wrapped);
    }

    private void handleValidatorTag(TagInfo ti,
                                    ValidatorTag validatorTag,
                                    Attributes attributes)
    {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(validatorTag);

        for (int i = 0, size = attributes.getLength(); i < size; i++)
        {
            String attrName = attributes.getLocalName(i);
            String attrValue = attributes.getValue(i);
            PropertyDescriptor propDescr = BeanUtils.findPropertyDescriptor(beanInfo, attrName);
            if (propDescr == null)
            {
                throw new RuntimeException("No PropertyDescriptor found for tag property " + attrName);
            }
            Object objValue = convertStringToTargetType(propDescr, attrValue);
            BeanUtils.setBeanPropertyValue(validatorTag, propDescr, objValue);
        }

        Validator validator;
        try
        {
            Method m = null;
            Class c = validatorTag.getClass();
            while (m == null && c != null && !c.equals(Object.class))
            {
                try
                {
                    m = c.getDeclaredMethod("createValidator",
                                            null);
                }
                catch (NoSuchMethodException e) {}
                c = c.getSuperclass();
            }

            if (m == null)
            {
                throw new NoSuchMethodException();
            }

            if (m.isAccessible())
            {
                validator = (Validator)m.invoke(validatorTag, null);
            }
            else
            {
                final Method finalM = m;
                AccessController.doPrivileged(
                    new PrivilegedAction()
                    {
                        public Object run()
                        {
                            finalM.setAccessible(true);
                            return null;
                        }
                    });
                validator = (Validator)m.invoke(validatorTag, null);
                m.setAccessible(false);
            }
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        catch (SecurityException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }

        _currentComponent.addValidator(validator);
    }


    private static final String FACET_NAME_ATTR = "name";
    private static final String PENDING_FACET_ATTR = "pending_facet";

    private void handleFacetTag(Attributes attrs)
    {
        String name = attrs.getValue(FACET_NAME_ATTR);
        if (name == null)
        {
            LogUtil.getLogger().severe("facet tag has no " + FACET_NAME_ATTR + " attribute!");
            return;
        }
        _currentComponent.setAttribute(PENDING_FACET_ATTR, name);
    }


}
