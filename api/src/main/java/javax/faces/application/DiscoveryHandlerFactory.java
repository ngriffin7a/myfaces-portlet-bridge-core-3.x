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
package javax.faces.application;

import javax.faces.FacesWrapper;

/**
 * TODO: REMOVE - No longer existing in latst spec 2009-03-04 -= Simon =-
 * 
 * @since 2.0
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 723192 $ $Date: 2008-12-03 21:31:00 -0500 (mer., 03 déc. 2008) $
 * 
 * @deprecated
 */
@Deprecated
public abstract class DiscoveryHandlerFactory implements
        FacesWrapper<DiscoveryHandlerFactory>
{

    public abstract DiscoveryHandler getDiscoveryHandler();

    /**
     * If this factory has been decorated, the implementation doing the decorating may override this method to 
     * provide access to the implementation being wrapped. A default implementation is provided that returns 
     * <code>null</code>.
     * 
     * @return the decorated <code>DiscoveryHandlerFactory</code> if this factory decorates another, 
     *         or <code>null</code> otherwise
     * 
     * @since 2.0
     */
    public DiscoveryHandlerFactory getWrapped()
    {
        return null;
    }

}