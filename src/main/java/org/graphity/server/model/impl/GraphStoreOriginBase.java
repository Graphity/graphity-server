/**
 *  Copyright 2014 Martynas Jusevičius <martynas@graphity.org>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.graphity.server.model.impl;

import org.graphity.server.model.GraphStoreOrigin;
import org.graphity.server.util.DataManager;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class GraphStoreOriginBase extends OriginBase implements GraphStoreOrigin
{

    public GraphStoreOriginBase(String uri, String authUser, String authPwd, DataManager dataManager)
    {
        super(uri);
        
        if (dataManager != null && authUser != null && authPwd != null)
            dataManager.putAuthContext(uri, authUser, authPwd);
    }
    
    public GraphStoreOriginBase(String uri)
    {
        this(uri, null, null, null);
    }
    
}
