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

package javax.faces.convert;

import org.apache.shale.test.base.AbstractJsfTestCase;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.Locale;

public class NumberConverterTest extends AbstractJsfTestCase
{
    private NumberConverter mock;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(NumberConverterTest.class);
    }

    public NumberConverterTest(String name)
    {
        super(name);
    }

    public void setUp()
    {
        super.setUp();

        mock = new NumberConverter();
    }

    public void tearDown()
    {
        super.tearDown();

        mock = null;
    }
    /*
     * temporarily comment out tests that fail, until Matthias Wessendorf has time to investigate
     */
    public void testFranceLocaleWithNonBreakingSpace()
    {
        mock.setLocale(Locale.FRANCE);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMANY);
        UIInput input = new UIInput();
        mock.setType("currency");
        String stringValue = mock.getAsString(facesContext, input, new Double(12345.68d));
        Number number = (Number) mock.getAsObject(FacesContext.getCurrentInstance(), input, "12\u00a0345,68 \u20AC");
        assertNotNull(number);
    }
    public void testFranceLocaleWithoutNonBreakingSpace()
    {
        mock.setLocale(Locale.FRANCE);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMANY);
        UIInput input = new UIInput();
        mock.setType("currency");
        Number number = (Number) mock.getAsObject(FacesContext.getCurrentInstance(), input, "12 345,68 \u20AC");
        assertNotNull(number);
    }
    
    /**
     * EUR12,345.68 
     */
    public void testUSLocaleUsingEURCurrencyCode()
    {
        facesContext.getViewRoot().setLocale(Locale.US);
        mock.setLocale(Locale.US);
        UIInput input = new UIInput();
        mock.setType("currency");
        mock.setCurrencyCode("EUR");
        Number testValue = new Double(12345.68d);
        String stringValue = mock.getAsString(facesContext, input, testValue);
        Number number = (Number) mock.getAsObject(facesContext, input, stringValue);
        assertNotNull(number);
        assertEquals(testValue, number);
    }

    /**
     * �12,345.68
     */
    public void testUKLocaleUsingEURCurrencyCode()
    {
        facesContext.getViewRoot().setLocale(Locale.US);
        mock.setLocale(Locale.UK);
        UIInput input = new UIInput();
        mock.setType("currency");
        mock.setCurrencyCode("EUR");
        Number testValue = new Double(12345.68d);
        String stringValue = mock.getAsString(facesContext, input, testValue);
        Number number = (Number) mock.getAsObject(facesContext, input, stringValue);
        assertNotNull(number);
        assertEquals(testValue, number);
    }
    
    /**
     * 12.345,68 �
     */
    public void testGermanyLocaleUsingEURCurrencyCode()
    {
        facesContext.getViewRoot().setLocale(Locale.US);
        mock.setLocale(Locale.GERMANY);
        UIInput input = new UIInput();
        mock.setType("currency");
        mock.setCurrencyCode("EUR");
        Number testValue = new Double(12345.68d);
        String stringValue = mock.getAsString(facesContext, input, testValue);
        Number number = (Number) mock.getAsObject(facesContext, input, stringValue);
        assertNotNull(number);
        assertEquals(testValue, number);
    }
    
    public void testCurrencyPattern()
    {
        facesContext.getViewRoot().setLocale(Locale.US);
        mock.setLocale(Locale.US);
        UIInput input = new UIInput();
        mock.setPattern("\u00A4 ###,###.###");
        Number testValue = new Double(12345.68d);
        String stringValue = mock.getAsString(facesContext, input, testValue);
        Number number = (Number) mock.getAsObject(facesContext, input, stringValue);
        assertNotNull(number);
        assertEquals(testValue, number);        
    }

    public void testCurrencyPattern2()
    {
        facesContext.getViewRoot().setLocale(Locale.US);
        mock.setLocale(Locale.GERMANY);
        UIInput input = new UIInput();
        mock.setPattern("\u00A4 ###,###.###");
        mock.setCurrencyCode("USD"); //Since currency is �, but we are using USD currency code, the output is USD 12.345,68
        Number testValue = new Double(12345.68d);
        String stringValue = mock.getAsString(facesContext, input, testValue);
        Number number = (Number) mock.getAsObject(facesContext, input, stringValue);
        assertNotNull(number);
        assertEquals(testValue, number);        
    }

}
