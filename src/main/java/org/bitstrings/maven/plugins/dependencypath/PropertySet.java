/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitstrings.maven.plugins.dependencypath;

import java.io.File;
import java.util.Set;

/**
 *
 * @author Pino Silvaggio
 *
 */
public class PropertySet
{
    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private String suffix;

    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private Set<String> includes;

    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private Set<String> excludes;

    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private File relativeTo;

    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private Boolean transitive;

    /**
     * @parameter
     *
     * @since 1.0.0
     */
    private Boolean autoRelativeSuffix;

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    public Set<String> getIncludes()
    {
        return includes;
    }

    public void setIncludes(Set<String> includes)
    {
        this.includes = includes;
    }

    public Set<String> getExcludes()
    {
        return excludes;
    }

    public void setExcludes(Set<String> excludes)
    {
        this.excludes = excludes;
    }

    public File getRelativeTo()
    {
        return relativeTo;
    }
    public void setRelativeTo(File relativeTo)
    {
        this.relativeTo = relativeTo;
    }

    public Boolean getTransitive()
    {
        return transitive;
    }

    public void setTransitive(Boolean transitive)
    {
        this.transitive = transitive;
    }

    public Boolean getAutoRelativeSuffix()
    {
        return autoRelativeSuffix;
    }

    public void setAutoRelativeSuffix(Boolean autoRelativeSuffix)
    {
        this.autoRelativeSuffix = autoRelativeSuffix;
    }

    @Override
    public String toString()
    {
        return
                "suffix: " + suffix
                    + ", includes: " + includes
                    + ", excludes: " + excludes
                    + ", relativeTo: " + relativeTo
                    + ", transitive: " + transitive
                    + ", autoRelativeSuffix: " + autoRelativeSuffix;
    }
}
