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
package org.apache.myfaces.view.facelets.tag.jsf;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagHandlerDelegate;

/**
 * @author Leonardo Uribe (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 * @since 2.0
 */
public class BehaviorTagHandlerDelegate extends TagHandlerDelegate implements AttachedObjectHandler
{

    private BehaviorHandler _delegate;
    
    public BehaviorTagHandlerDelegate(BehaviorHandler delegate)
    {
        _delegate = delegate;
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent comp) throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MetaRuleset createMetaRuleset(Class<?> type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void applyAttachedObject(FacesContext context, UIComponent parent)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getFor()
    {
        // TODO Auto-generated method stub
        return null;
    }

}