/**
 *  Copyright 2014 Martynas Jusevičius <martynas@atomgraph.com>
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

package com.atomgraph.core.model.impl.remote;

import org.apache.jena.rdf.model.Model;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import com.atomgraph.core.MediaTypes;
import com.atomgraph.core.client.GraphStoreClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy implementation of Graph Store.
 * This class forwards requests to a remote origin.
 * 
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
//@Path("/service") // not standard
public class GraphStoreBase extends com.atomgraph.core.model.impl.GraphStoreBase implements com.atomgraph.core.model.remote.GraphStore
{
    private static final Logger log = LoggerFactory.getLogger(GraphStoreBase.class);

    private final String uri;
    private final GraphStoreClient graphStoreClient;
    
    /**
     * Constructs Graph Store proxy from request metadata and origin URI.
     * 
     * @param client HTTP client
     * @param mediaTypes supported media types
     * @param uri graph store URI
     * @param authUser HTTP Basic username
     * @param authPwd HTTP Basic password
     * @param request HTTP request
     */
    public GraphStoreBase(@Context Client client, @Context MediaTypes mediaTypes, String uri, String authUser, String authPwd, @Context Request request)
    {
        super(request, mediaTypes);
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        if (uri == null) throw new IllegalArgumentException("URI string cannot be null");
        this.uri = uri;
        this.graphStoreClient = GraphStoreClient.create(client.resource(uri));
        
        if (authUser != null && authPwd != null)
        {
            ClientFilter authFilter = new HTTPBasicAuthFilter(authUser, authPwd);
            this.graphStoreClient.getWebResource().addFilter(authFilter);
        }
    }
    
    @Override
    public Model getModel()
    {
        return getGraphStoreClient().getModel();
    }

    @Override
    public Model getModel(String uri)
    {
        return getGraphStoreClient().getModel(uri);
    }

    @Override
    public boolean containsModel(String uri)
    {
        return getGraphStoreClient().containsModel(uri);
    }
    
    @Override
    public void putModel(Model model)
    {
        getGraphStoreClient().putModel(model);
    }

    @Override
    public void putModel(String uri, Model model)
    {
        getGraphStoreClient().putModel(uri, model);
    }

    @Override
    public void deleteDefault()
    {
        getGraphStoreClient().deleteDefault();
    }

    @Override
    public void deleteModel(String uri)
    {
        getGraphStoreClient().deleteModel(uri);
    }

    @Override
    public void add(Model model)
    {
        getGraphStoreClient().add(model);
    }

    @Override
    public void add(String uri, Model model)
    {
        getGraphStoreClient().add(uri, model);
    }

    @Override
    public String getURI()
    {
        return uri;
    }
    
    @Override
    public GraphStoreClient getGraphStoreClient()
    {
        return graphStoreClient;
    }
    
}