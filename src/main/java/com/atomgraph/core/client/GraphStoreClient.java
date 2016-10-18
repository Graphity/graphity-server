/*
 * Copyright 2016 Martynas Jusevičius <martynas@atomgraph.com>.
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
package com.atomgraph.core.client;

import com.atomgraph.core.client.simple.SimpleGraphStoreClient;
import com.atomgraph.core.MediaType;
import com.atomgraph.core.MediaTypes;
import com.atomgraph.core.exception.ClientException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.Response;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
public class GraphStoreClient extends SimpleGraphStoreClient implements DatasetAccessor
{
    private static final Logger log = LoggerFactory.getLogger(GraphStoreClient.class);

    private final MediaTypes mediaTypes;
    
    protected GraphStoreClient(WebResource webResource, MediaTypes mediaTypes)
    {
        super(webResource);
        this.mediaTypes = mediaTypes;
    }

    protected GraphStoreClient(WebResource webResource)
    {
        this(webResource, new MediaTypes());
    }

    public static GraphStoreClient create(WebResource webResource, MediaTypes mediaTypes)
    {
        return new GraphStoreClient(webResource, mediaTypes);
    }

    public static GraphStoreClient create(WebResource webResource)
    {
        return new GraphStoreClient(webResource);
    }

    @Override
    public Model getModel()
    {
	if (log.isDebugEnabled()) log.debug("GET Model from Graph Store {} default graph", getWebResource().getURI());
	ClientResponse cr = get(getReadableMediaTypes(Model.class));
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
        
        return cr.getEntity(Model.class);
    }

    @Override
    public Model getModel(String uri)
    {
	if (log.isDebugEnabled()) log.debug("GET Model from Graph Store {} with named graph URI: {}", getWebResource().getURI(), uri);
	ClientResponse cr = get(getReadableMediaTypes(Model.class), uri);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
        
        return cr.getEntity(Model.class);
    }

    @Override
    public boolean containsModel(String uri)
    {
	if (log.isDebugEnabled()) log.debug("Checking if Graph Store {} contains GRAPH with URI {}", getWebResource().getURI(), uri);
	ClientResponse cr = head(uri);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }

        return true;
    }
    
    @Override
    public void putModel(Model model)
    {
	if (log.isDebugEnabled()) log.debug("PUT Model to Graph Store {} default graph", getWebResource().getURI());
	ClientResponse cr = put(MediaType.TEXT_NTRIPLES_TYPE, model);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    @Override
    public void putModel(String uri, Model model)
    {
	if (log.isDebugEnabled()) log.debug("PUT Model to Graph Store {} with named graph URI {}", getWebResource().getURI(), uri);
	ClientResponse cr = putModel(MediaType.TEXT_NTRIPLES_TYPE, uri, model);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    @Override
    public void deleteDefault()
    {
	if (log.isDebugEnabled()) log.debug("DELETE default graph from Graph Store {}", getWebResource().getURI());
	ClientResponse cr = delete();
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    @Override
    public void deleteModel(String uri)
    {
	if (log.isDebugEnabled()) log.debug("DELETE named graph with URI {} from Graph Store {}", uri, getWebResource().getURI());
	ClientResponse cr = delete(uri);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    @Override
    public void add(Model model)
    {
	if (log.isDebugEnabled()) log.debug("POST Model to Graph Store {} default graph", getWebResource().getURI());
	ClientResponse cr = add(MediaType.TEXT_NTRIPLES_TYPE, model);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    
    @Override
    public void add(String uri, Model model)
    {
	if (log.isDebugEnabled()) log.debug("POST Model to Graph Store {} with named graph URI: {}", getWebResource().getURI(), uri);
	ClientResponse cr = add(MediaType.TEXT_NTRIPLES_TYPE, uri, model);
        if (!cr.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL))
        {
            if (log.isDebugEnabled()) log.debug("Request to graph store: {} unsuccessful. Reason: {}", getWebResource().getURI(), cr.getStatusInfo().getReasonPhrase());
            throw new ClientException(cr);
        }
    }

    public MediaTypes getMediaTypes()
    {
        return mediaTypes;
    }

    public javax.ws.rs.core.MediaType[] getReadableMediaTypes(Class clazz)
    {
        return getMediaTypes().getReadable(clazz).toArray(new javax.ws.rs.core.MediaType[0]);
    }

}