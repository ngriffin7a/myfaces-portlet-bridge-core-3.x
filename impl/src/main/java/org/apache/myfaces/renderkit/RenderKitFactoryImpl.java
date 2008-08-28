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
package org.apache.myfaces.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * RenderKitFactory implementation as defined in Spec. JSF.7.3
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitFactoryImpl
    extends RenderKitFactory
{
    private static final Log log = LogFactory.getLog(RenderKitFactoryImpl.class);

    private Map<String, RenderKit> _renderkits = new HashMap<String, RenderKit>();

    public RenderKitFactoryImpl()
    {
    }

    public void purgeRenderKit()
    {
        _renderkits.clear();
    }
    
    public void addRenderKit(String renderKitId, RenderKit renderKit)
    {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        if (renderKit == null) throw new NullPointerException("renderKit");
        if (log.isInfoEnabled())
        {
            if (_renderkits.containsKey(renderKitId))
            {
                log.info("RenderKit with renderKitId '" + renderKitId + "' was replaced.");
            }
        }
        _renderkits.put(renderKitId, renderKit);
    }


    public RenderKit getRenderKit(FacesContext context, String renderKitId)
            throws FacesException
    {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        RenderKit renderkit = _renderkits.get(renderKitId);
        if (renderkit == null)
        {
            //throw new IllegalArgumentException("Unknown RenderKit '" + renderKitId + "'.");
            //JSF Spec API Doc says:
            // "If there is no registered RenderKit for the specified identifier, return null"
            // vs "IllegalArgumentException - if no RenderKit instance can be returned for the specified identifier"
            //First sentence is more precise, so we just log a warning
            log.warn("Unknown RenderKit '" + renderKitId + "'.");
        }
        return renderkit;
    }


    public Iterator<String> getRenderKitIds()
    {
        return _renderkits.keySet().iterator();
    }
}
