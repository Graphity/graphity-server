/*
 * Copyright 2015 Martynas Jusevičius <martynas@atomgraph.com>.
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

package com.atomgraph.core.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;
import com.sun.jersey.api.uri.UriComponent;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Martynas Jusevičius <martynas@atomgraph.com>
 */
public class StateBuilder
{
    private final Resource resource;
    private final UriBuilder uriBuilder;
    
    protected StateBuilder(Resource resource)
    {
	if (resource == null) throw new IllegalArgumentException("Resource cannot be null");
        if (!resource.isURIResource()) throw new IllegalArgumentException("Resource must be URI resource");
        
        this.resource = resource;
        this.uriBuilder = UriBuilder.fromUri(resource.getURI());
    }

    public static StateBuilder fromUri(String uri, Model model)
    {
        if (uri == null) throw new IllegalArgumentException("URI String cannot be null");        
        if (model == null) throw new IllegalArgumentException("Model cannot be null");        
        
        return new StateBuilder(model.createResource(uri));
    }

    public static StateBuilder fromResource(Resource resource)
    {
        return new StateBuilder(resource);
    }
    
    protected Resource getResource()
    {
        return resource;
    }
    
    protected UriBuilder getUriBuilder()
    {
        return uriBuilder;
    }

    public StateBuilder property(Property property, RDFNode value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        
        if (value == null) throw new IllegalArgumentException("RDFNode cannot be null");        

        getResource().addProperty(property, value);
        String encodedValue = value.toString(); // not a reliable serialization
        // we URI-encode values ourselves because Jersey 1.x fails to do so: https://java.net/jira/browse/JERSEY-1717
        if (value.isURIResource()) encodedValue = UriComponent.encode(value.asResource().getURI(), UriComponent.Type.UNRESERVED);
        if (value.isLiteral()) encodedValue = UriComponent.encode(value.asLiteral().getString(), UriComponent.Type.UNRESERVED);
        getUriBuilder().queryParam(property.getLocalName(), encodedValue);
        
        return this;
    }

    public StateBuilder replaceProperty(Property property, RDFNode value)
    {
        if (property == null) throw new IllegalArgumentException("Property cannot be null");        

        getResource().removeAll(property);
        getUriBuilder().replaceQueryParam(property.getLocalName(), (Object[])null);
        
        if (value != null) property(property, value);
        
        return this;
    }
    
    public Resource build()
    {
        return ResourceUtils.renameResource(getResource(), getUriBuilder().build().toString());
    }

    @Override
    public String toString()
    {
        return getResource().listProperties().toList().toString();
    }
    
}
