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

package org.apache.myfaces.view.facelets.tag.composite;

import java.io.StringWriter;

import javax.el.MethodExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;

import org.apache.myfaces.test.mock.MockResponseWriter;
import org.apache.myfaces.test.utils.HtmlCheckAttributesUtil;
import org.apache.myfaces.test.utils.HtmlRenderedAttr;
import org.apache.myfaces.view.facelets.FaceletTestCase;
import org.apache.myfaces.view.facelets.bean.HelloWorld;

public class CompositeComponentTestCase extends FaceletTestCase
{

    /**
     * Test if a child component inside composite component template is
     * rendered.
     * 
     * @throws Exception
     */
    public void testSimpleCompositeComponent() throws Exception
    {
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleComposite.xhtml");

        UIComponent panelGroup = root.findComponent("testGroup");
        assertNotNull(panelGroup);
        UINamingContainer compositeComponent = (UINamingContainer) panelGroup.getChildren().get(0);
        assertNotNull(compositeComponent);
        UIOutput text = (UIOutput) compositeComponent.getFacet(UIComponent.COMPOSITE_FACET_NAME).findComponent("text");
        assertNotNull(text);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        compositeComponent.encodeAll(facesContext);
        sw.flush();
        
        HtmlRenderedAttr[] attrs = new HtmlRenderedAttr[]{
                new HtmlRenderedAttr("value")
        };
            
        HtmlCheckAttributesUtil.checkRenderedAttributes(attrs, sw.toString());
    }

    /**
     * Test simple attribute resolution (not set, default, normal use case).
     * 
     * @throws Exception
     */
    public void testSimpleCompositeAttribute() throws Exception
    {
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleAttribute.xhtml");

        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        HtmlOutputText text1 = (HtmlOutputText) facet1.findComponent("text");
        assertNotNull(text1);
        
        compositeComponent1.pushComponentToEL(facesContext, compositeComponent1);
        facet1.pushComponentToEL(facesContext, facet1);
        text1.pushComponentToEL(facesContext, text1);
        //set on tag
        assertEquals("class1", text1.getStyleClass());
        //set as default
        assertEquals("background:red", text1.getStyle());
        text1.popComponentFromEL(facesContext);
        facet1.popComponentFromEL(facesContext);
        compositeComponent1.popComponentFromEL(facesContext);
        
        UIComponent panelGroup2 = root.findComponent("testGroup2");
        assertNotNull(panelGroup2);
        UINamingContainer compositeComponent2 = (UINamingContainer) panelGroup2.getChildren().get(0);
        assertNotNull(compositeComponent2);
        UIComponent facet2 = compositeComponent2.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet2);        
        HtmlOutputText text2 = (HtmlOutputText) facet2.findComponent("text");
        assertNotNull(text2);
        
        compositeComponent2.pushComponentToEL(facesContext, compositeComponent2);
        facet2.pushComponentToEL(facesContext, facet2);
        text2.pushComponentToEL(facesContext, text2);
        //set on tag
        assertEquals("background:green", text2.getStyle());
        // not set, should return null, but since there is a ValueExpression indirection,
        // coercing rules apply here, so null is converted as ""
        assertEquals("", text2.getStyleClass());
        text2.popComponentFromEL(facesContext);
        facet2.popComponentFromEL(facesContext);
        compositeComponent2.popComponentFromEL(facesContext);

        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        compositeComponent1.encodeAll(facesContext);
        sw.flush();
        
        HtmlRenderedAttr[] attrs = new HtmlRenderedAttr[]{
                new HtmlRenderedAttr("style")
        };
            
        HtmlCheckAttributesUtil.checkRenderedAttributes(attrs, sw.toString());
    }
    
    public void testSimpleCompositeAttributeMethodExpression() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleAttributeMethodExpression.xhtml");

        UIComponent form = root.findComponent("testForm1");
        assertNotNull(form);
        UINamingContainer compositeComponent = (UINamingContainer) form.getChildren().get(0);
        assertNotNull(compositeComponent);
        UICommand button = (UICommand) compositeComponent.findComponent("button");
        assertNotNull(button);
        assertEquals("#{helloWorldBean.send}", button.getActionExpression().getExpressionString());
        assertEquals("#{helloWorldBean.send}", ((MethodExpression)compositeComponent.getAttributes().get("metodo")).getExpressionString());
        assertNull(button.getAttributes().get("metodo"));
        
        UICommand link = (UICommand) compositeComponent.findComponent("link");
        assertNotNull(link);
        assertEquals(1, link.getActionListeners().length);
        UIInput input = (UIInput) compositeComponent.findComponent("input");
        assertNotNull(input);
        assertEquals(1, input.getValidators().length);
        assertEquals(1, input.getValueChangeListeners().length);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        //root.encodeAll(facesContext);
        //compositeComponent.encodeAll(facesContext);
        //sw.flush();
        //System.out.print(sw.toString());
    }
    
    public void testSimpleActionSource() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleActionSource.xhtml");
        
        UIComponent form = root.findComponent("testForm1");
        assertNotNull(form);
        UINamingContainer compositeComponent = (UINamingContainer) form.getChildren().get(0);
        assertNotNull(compositeComponent);
        UICommand button = (UICommand) compositeComponent.findComponent("button");
        assertNotNull(button);
        assertEquals(3, button.getActionListeners().length);
        
        //StringWriter sw = new StringWriter();
        //MockResponseWriter mrw = new MockResponseWriter(sw);
        //facesContext.setResponseWriter(mrw);
        
        //root.encodeAll(facesContext);
        //sw.flush();
        //System.out.print(sw.toString());
    }
    
    public void testSimpleValueHolder() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleValueHolder.xhtml");
        
        UIComponent form = root.findComponent("testForm1");
        assertNotNull(form);
        UINamingContainer compositeComponent = (UINamingContainer) form.getChildren().get(0);
        assertNotNull(compositeComponent);
        UIOutput text = (UIOutput) compositeComponent.findComponent("text");
        assertNotNull(text);
        assertNotNull(text.getConverter());
        //assertEquals(2, button.getActionListeners().length);
        
        //StringWriter sw = new StringWriter();
        //MockResponseWriter mrw = new MockResponseWriter(sw);
        //facesContext.setResponseWriter(mrw);
        
        //root.encodeAll(facesContext);
        //sw.flush();
        //System.out.print(sw.toString());
    }
    
    public void testCompositeActionSource() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeActionSource.xhtml");
        
        UIComponent form = root.findComponent("testForm1");
        assertNotNull(form);
        UINamingContainer compositeComponent = (UINamingContainer) form.getChildren().get(0);
        assertNotNull(compositeComponent);
        UINamingContainer compositeComponent2 = (UINamingContainer) compositeComponent.findComponent("button3");
        assertNotNull(compositeComponent2);
        UICommand button = (UICommand) compositeComponent2.findComponent("button");
        assertNotNull(button);
        //One added in testCompositeActionSource, the other one
        //inside compositeActionSource.xhtml
        assertEquals(2, button.getActionListeners().length);
        
        //StringWriter sw = new StringWriter();
        //MockResponseWriter mrw = new MockResponseWriter(sw);
        //facesContext.setResponseWriter(mrw);
        
        //root.encodeAll(facesContext);
        //sw.flush();
        //System.out.print(sw.toString());
    }
    
    public void testSimpleInsertChildren() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleInsertChildren.xhtml");
        
        /*
        UIComponent form = root.findComponent("testForm1");
        assertNotNull(form);
        UINamingContainer compositeComponent = (UINamingContainer) form.getChildren().get(0);
        assertNotNull(compositeComponent);
        UINamingContainer compositeComponent2 = (UINamingContainer) compositeComponent.findComponent("button3");
        assertNotNull(compositeComponent2);
        UICommand button = (UICommand) compositeComponent2.findComponent("button");
        assertNotNull(button);
        */
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        root.encodeAll(facesContext);
        sw.flush();

        String resp = sw.toString();
        
        assertTrue(resp.contains("Hello"));
        assertTrue(resp.contains("Leonardo"));
        assertTrue(resp.contains("Alfredo"));
        assertTrue(resp.contains("Uribe"));
        assertTrue(resp.contains("Sayonara"));
        //System.out.print(sw.toString());
    }
    
    public void testSimpleInsertChildrenAjax() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleInsertChildrenAjax.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        HtmlCommandLink link = (HtmlCommandLink) facet1.findComponent("link");
        assertNotNull(link);
        assertEquals(1, link.getClientBehaviors().size());
        assertEquals(1, link.getClientBehaviors().get(link.getDefaultEventName()).size());
        /*
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        root.encodeAll(facesContext);
        sw.flush();

        String resp = sw.toString();
        */
        //System.out.print(sw.toString());
    }

    public void testSimpleInsertChildrenAjax2() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleInsertChildrenAjax2.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        HtmlCommandLink link = (HtmlCommandLink) compositeComponent1.findComponent("link");
        assertNotNull(link);
        assertEquals(1, link.getClientBehaviors().size());
        assertEquals(1, link.getClientBehaviors().get(link.getDefaultEventName()).size());
        /*
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        root.encodeAll(facesContext);
        sw.flush();

        String resp = sw.toString();
        */
        //System.out.print(sw.toString());
    }
    
    public void testSimpleInsertChildrenNoAjax() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleInsertChildrenNoAjax.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        HtmlCommandLink link = (HtmlCommandLink) facet1.findComponent("link");
        assertNotNull(link);
        assertEquals(0, link.getClientBehaviors().size());
        /*
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);
        
        root.encodeAll(facesContext);
        sw.flush();

        String resp = sw.toString();
        */
        //System.out.print(sw.toString());
    }
    
    public void testCompositeInsertChildren() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertChildren.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        facet1.encodeAll(facesContext);

        sw.flush();
        
        String resp = sw.toString();

        assertTrue(resp.contains("ALFA"));
        assertTrue(resp.contains("BETA"));
        assertTrue(resp.contains("GAMMA"));
        assertTrue(resp.contains("OMEGA"));
    }
    
    public void testCompositeInsertChildrenPreserveTemplateSlot() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertChildren2.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        facet1.encodeAll(facesContext);

        sw.flush();
        
        String resp = sw.toString();

        assertTrue(resp.contains("ALFA"));
        assertTrue(resp.contains("BETA"));
        assertTrue(resp.contains("GAMMA"));
        assertTrue(resp.contains("OMEGA"));
    }
    
    public void testCompositeInsertChildren3() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertChildren3.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        panelGroup1.encodeAll(facesContext);

        sw.flush();
        
        String resp = sw.toString();

        assertTrue(resp.contains("ALFA"));
        assertTrue(resp.contains("BETA"));
        assertTrue(resp.contains("GAMMA"));
        assertTrue(resp.contains("OMEGA"));
    }
    
    public void testCompositeInsertChildren4() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertChildren4.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        //UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        //assertNotNull(compositeComponent1);
        //UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        //assertNotNull(facet1);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        panelGroup1.encodeAll(facesContext);

        sw.flush();
        
        String resp = sw.toString();

        assertTrue(resp.contains("ALFA"));
        assertTrue(resp.contains("BETA"));
        assertTrue(resp.contains("GAMMA"));
        assertTrue(resp.contains("OMEGA"));
    }
    
    public void testCompositeInsertFacet() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertFacet.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        
        UINamingContainer compositeComponent2 = (UINamingContainer) facet1.getChildren().get(0);
        assertNotNull(compositeComponent2);
        UIComponent facet2 = compositeComponent2.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet2);
        assertEquals(1,facet2.getChildCount());
        UIOutput targetComp = (UIOutput) facet2.getChildren().get(0);
        UIComponent insertedFacet = targetComp.getFacet("header");
        assertNotNull(insertedFacet);
    }
    
    public void testCompositeInsertFacetChildren() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testCompositeInsertFacetChildren.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        UIComponent facet1 = compositeComponent1.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet1);
        
        UINamingContainer compositeComponent2 = (UINamingContainer) facet1.getChildren().get(0);
        assertNotNull(compositeComponent2);
        UIComponent facet2 = compositeComponent2.getFacet(UIComponent.COMPOSITE_FACET_NAME);
        assertNotNull(facet2);
        assertEquals(3,facet2.getChildCount());
        UIComponent insertedFacet = facet2.getChildren().get(1).getFacet("header");
        assertNotNull(insertedFacet);
    }

    public void testSimpleRenderFacet() throws Exception
    {
        HelloWorld helloWorld = new HelloWorld(); 
        
        facesContext.getExternalContext().getRequestMap().put("helloWorldBean",
                helloWorld);
        
        UIViewRoot root = facesContext.getViewRoot();
        vdl.buildView(facesContext, root, "testSimpleRenderFacet.xhtml");
        
        UIComponent panelGroup1 = root.findComponent("testGroup1");
        assertNotNull(panelGroup1);
        UINamingContainer compositeComponent1 = (UINamingContainer) panelGroup1.getChildren().get(0);
        assertNotNull(compositeComponent1);
        
        StringWriter sw = new StringWriter();
        MockResponseWriter mrw = new MockResponseWriter(sw);
        facesContext.setResponseWriter(mrw);

        compositeComponent1.encodeAll(facesContext);

        sw.flush();
        
        String resp = sw.toString();

        assertTrue(resp.contains("HELLO"));
        assertTrue(resp.contains("WORLD"));
        
    }
}