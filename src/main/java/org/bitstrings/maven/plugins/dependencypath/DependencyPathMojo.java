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
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.PathTool;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoMultiExecution;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoPhase;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 *
 * @author Pino Silvaggio
 *
 */
@MojoRequiresDependencyResolution("test")
@MojoGoal("set")
@MojoPhase("initialize")
@MojoMultiExecution
public class DependencyPathMojo extends AbstractMojo
{
    @MojoParameter(expression = "${project}", required = true, readonly = true,
                   description = "The Maven Project")
    private MavenProject project;

    @MojoParameter
    private PropertySet propertySetsDefault;

    @MojoParameter
    private PropertySet[] propertySets;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (propertySetsDefault == null)
        {
            propertySetsDefault = new PropertySet();
        }

        if (propertySets == null)
        {
            propertySets = new PropertySet[] { new PropertySet() };
        }

        if (propertySetsDefault.getAutoRelativeSuffix() == null)
        {
            propertySetsDefault.setAutoRelativeSuffix(true);
        }

        if (propertySetsDefault.getTransitive() == null)
        {
            propertySetsDefault.setTransitive(true);
        }

        final Properties properties = project.getProperties();

        for (PropertySet propertySet : propertySets)
        {
            final Set<String> includes = propertySet.getIncludes();
            final Set<String> excludes = propertySet.getExcludes();

            Boolean transitive = propertySet.getTransitive();

            if (transitive == null)
            {
                transitive = propertySetsDefault.getTransitive();
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
                    relativeTo = propertySetsDefault.getRelativeTo();
                }

                if (relativeTo != null)
                {
                    Boolean autoRelativeSuffix = propertySet.getAutoRelativeSuffix();

                    if (autoRelativeSuffix == null)
                    {
                        autoRelativeSuffix = propertySetsDefault.getAutoRelativeSuffix();
                    }

                    if (autoRelativeSuffix)
                    {
                        key += ".relative";
                    }
                }

                String suffix = propertySet.getSuffix();

                if (suffix == null)
                {
                    suffix = propertySetsDefault.getSuffix();
                }

                if (suffix != null)
                {
                    key += "." + suffix;
                }

                properties.setProperty(key, getPath(artifact, relativeTo));

                if (getLog().isDebugEnabled())
                {
                    getLog().debug("Setting property " + key + "=" + properties.get(key));
                }
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
