/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.tiles.taglib;

import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import java.io.IOException;

/**
 * This is the tag handler for &lt;tiles:attribute&gt;, which defines an
 * attribute. If the attribute value is a template or a definition, its
 * attributes and its template can be overridden.
 *
 * @version $Rev$ $Date$
 */
public class InsertAttributeTag extends RenderTagSupport {

    /**
     * Name to insert.
     */
    protected String name;
    
    protected Object value = null;
    
    protected ComponentContext evaluatingContext;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    /**
     * Get the value.
     *
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The new value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    public void release() {
        super.release();
        this.name = null;
        this.value = null;
    }

    protected void render() throws JspException, TilesException, IOException {
        ComponentAttribute attr = (ComponentAttribute) value;
        if (attr == null && evaluatingContext != null) {
            attr = evaluatingContext.getAttribute(name);
        }
        if (attr == null && ignore) {
            return;
        }

        if (attr == null) {
            if (name != null) {
                throw new TilesException("Attribute '" + name + "' not found.");
            } else {
                throw new TilesException("No attribute name or value has been provided.");
            }
        }
        render(attr);
    }

    @Override
    protected void startContext(PageContext context) {
        
        if (container != null) {
            evaluatingContext = container.getComponentContext(context);
        }
        super.startContext(context);
    }
    
    /**
     * Renders an attribute for real.
     *
     * @param attr The attribute to render.
     * @throws TilesException If something goes wrong during rendering.
     * @throws IOException If something goes wrong during the reading of
     * definition files.
     */
    protected void render(ComponentAttribute attr)
        throws TilesException, IOException {
        container.render(pageContext, attr);
    }
}
