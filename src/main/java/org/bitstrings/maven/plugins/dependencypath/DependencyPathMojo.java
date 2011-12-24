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
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.PathTool;

/**
 * Sets a property pointing to the artifact file for each selected project dependency.
 * Each property name will have a base name in form of groupId:artifactId:type:[classifier][.relative][.suffix].
 *
 * @requiresDependencyResolution test
 * @goal set
 * @phase initialize
 * @threadSafe
 *
 * @author Pino Silvaggio
 *
 */
public class DependencyPathMojo extends AbstractMojo
{
    /**
     * @parameter expression = "${project}"
     * @required
     * @readonly
     *
     * @since 1.0.0
     */
    private MavenProject project;

    /**
     * The default property set.
     * See <a href="propertyset-reference.html">PropertySet Reference</a>.
     *
     * @parameter
     *
     * @since 1.0.0
     */
    private PropertySet defaultPropertySet;

    /**
     * The property sets.
     * See <a href="propertyset-reference.html">PropertySet Reference</a>.
     *
     * @parameter
     *
     * @since 1.0.0
     */
    private PropertySet[] propertySets;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (defaultPropertySet == null)
        {
            defaultPropertySet = new PropertySet();
        }

        if (propertySets == null)
        {
            propertySets = new PropertySet[] { new PropertySet() };
        }

        if (defaultPropertySet.getAutoRelativeSuffix() == null)
        {
            defaultPropertySet.setAutoRelativeSuffix(true);
        }

        if (defaultPropertySet.getTransitive() == null)
        {
            defaultPropertySet.setTransitive(true);
        }

        if (getLog().isDebugEnabled())
        {
            getLog().debug("defaultPropertySet" + defaultPropertySet);
            getLog().debug("propertySets" + Arrays.toString(propertySets));
        }

        final Properties properties = project.getProperties();

        for (PropertySet propertySet : propertySets)
        {
            final Set<String> includes = propertySet.getIncludes();
            final Set<String> excludes = propertySet.getExcludes();

            Boolean transitive = propertySet.getTransitive();

            if (transitive == null)
            {
                transitive = defaultPropertySet.getTransitive();
            }

            final Set<Artifact> artifacts =
                            transitive
                                    ? project.getArtifacts()
                                    : project.getDependencyArtifacts();

            for (Artifact artifact : artifacts)
            {
                final String dependencyConflictId = artifact.getDependencyConflictId();

                if (((includes != null) && !includes.isEmpty()
                                && !includes.contains(dependencyConflictId))
                        || ((excludes != null) && !excludes.isEmpty()
                                        && excludes.contains(dependencyConflictId)))
                {
                    continue;
                }

                String key = dependencyConflictId;

                File relativeTo = propertySet.getRelativeTo();

                if (relativeTo == null)
                {
                    relativeTo = defaultPropertySet.getRelativeTo();
                }

                if (relativeTo != null)
                {
                    Boolean autoRelativeSuffix = propertySet.getAutoRelativeSuffix();

                    if (autoRelativeSuffix == null)
                    {
                        autoRelativeSuffix = defaultPropertySet.getAutoRelativeSuffix();
                    }

                    if (autoRelativeSuffix)
                    {
                        key += ".relative";
                    }
                }

                String suffix = propertySet.getSuffix();

                if (suffix == null)
                {
                    suffix = defaultPropertySet.getSuffix();
                }

                if (suffix != null)
                {
                    key += "." + suffix;
                }

                final String path = getPath(artifact, relativeTo);

                if (getLog().isDebugEnabled())
                {
                    getLog().debug(
                            "Setting property for " + dependencyConflictId
                                + " with key=" + key + ", path=" + path);
                }

                if (path == null)
                {
                    throw new MojoExecutionException(
                            "Unable to obtain path for " + dependencyConflictId
                                + (relativeTo == null ? "(absolute)" : "(relative to " + relativeTo + ")")
                                + ".");
                }

                properties.setProperty(key, path);
            }
        }
    }

    protected String getPath(Artifact artifact, File relativeTo)
    {
        return
                relativeTo == null
                        ? artifact.getFile().getAbsolutePath()
                        : PathTool.getRelativeFilePath(relativeTo.getPath(), artifact.getFile().getAbsolutePath());
    }
}
