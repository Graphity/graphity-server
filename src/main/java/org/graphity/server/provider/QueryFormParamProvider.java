/**
 *  Copyright 2012 Martynas Jusevičius <martynas@graphity.org>
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
package org.graphity.server.provider;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.PerRequestTypeInjectableProvider;
import java.lang.reflect.Type;
import javax.ws.rs.FormParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class QueryFormParamProvider extends PerRequestTypeInjectableProvider<FormParam, Query>
{
    private static final Logger log = LoggerFactory.getLogger(QueryFormParamProvider.class);
    
    @Context HttpContext httpContext;
    
    public QueryFormParamProvider(Type t)
    {
	super(t);
    }

    @Override
    public Injectable<Query> getInjectable(ComponentContext cc, FormParam fp)
    {
	final String paramName = fp.value();
	return new Injectable<Query>()
	{
	    @Override
	    public Query getValue()
	    {
		String value = getHttpContext().getRequest().getFormParameters().getFirst(paramName);
		if (value == null || value.isEmpty()) return null;
		    
		if (log.isTraceEnabled()) log.trace("Providing Injectable<Query> with @FormParam({}) and value: {}", paramName, value);
		try
		{
		    return QueryFactory.create(value);
		}
		catch (Exception ex)
		{
		    if (log.isWarnEnabled()) log.warn("Supplied SPARQL query string could not be parsed, check syntax: {}", value);
		    throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
		}
	    }
	};
    }

    public HttpContext getHttpContext()
    {
	return httpContext;
    }
    
}
