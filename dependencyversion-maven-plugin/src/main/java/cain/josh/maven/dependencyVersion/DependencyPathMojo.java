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
package cain.josh.maven.dependencyVersion;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * Sets a property pointing to the artifact version for each selected project dependency.
 * Each property name will have a base name in form of groupId:artifactId:type:[classifier][.suffix]
 *
 * For instance, the following dependency (as declared in a parent pom or BOM):
 *
 * <dependency>
 *   <groupId>io.undertow</groupId>
 *   <artifactId>undertow-core</artifactId>
 *   <version>1.3.15.Final</version>
 * </dependency>
 *
 * Would have base name of:
 *
 * io.undertow:undertow-core:jar
 */
@Mojo(name = "set-version",
    defaultPhase = LifecyclePhase.INITIALIZE,
    requiresDependencyResolution = ResolutionScope.TEST,
    threadSafe = true)
public class DependencyPathMojo extends AbstractMojo {

    /**
     * Should always be supplied by the Maven runner - provides the Maven project structure + dependency mappings
     */
    @Parameter(alias = "project",
            property = "project",
            required = true,
            readonly = true)
    private MavenProject project;

    /**
     * The default property set.
     * See <a href="propertyset-reference.html">PropertySet Reference</a>.
     */
    @Parameter
    private PropertySet defaultPropertySet;

    /**
     * The property sets.
     * See <a href="propertyset-reference.html">PropertySet Reference</a>.
     */
    @Parameter
    private PropertySet[] propertySets;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (defaultPropertySet == null) {
            defaultPropertySet = new PropertySet();
        }

        if (propertySets == null) {
            propertySets = new PropertySet[]{new PropertySet()};
        }

        if (defaultPropertySet.getAutoRelativeSuffix() == null) {
            defaultPropertySet.setAutoRelativeSuffix(true);
        }

        if (defaultPropertySet.getTransitive() == null) {
            defaultPropertySet.setTransitive(true);
        }

        if (getLog().isDebugEnabled()) {
            getLog().debug("defaultPropertySet" + defaultPropertySet);
            getLog().debug("propertySets" + Arrays.toString(propertySets));
        }

        final Properties properties = project.getProperties();

        for (PropertySet propertySet : propertySets) {
            final Set<String> includes = propertySet.getIncludes();
            final Set<String> excludes = propertySet.getExcludes();

            Boolean transitive = propertySet.getTransitive();

            if (transitive == null) {
                transitive = defaultPropertySet.getTransitive();
            }

            final Set<Artifact> artifacts =
                    transitive
                            ? project.getArtifacts()
                            : project.getDependencyArtifacts();

            for (Artifact artifact : artifacts) {
                final String dependencyConflictId = artifact.getDependencyConflictId();

                if (((includes != null) && !includes.isEmpty()
                        && !includes.contains(dependencyConflictId))
                        || ((excludes != null) && !excludes.isEmpty()
                        && excludes.contains(dependencyConflictId))) {
                    continue;
                }

                String key = dependencyConflictId;

                String suffix = propertySet.getSuffix();

                if (suffix == null) {
                    suffix = defaultPropertySet.getSuffix();
                }

                if (suffix != null) {
                    key += "." + suffix;
                }

                final String path = artifact.getVersion();
                if (getLog().isDebugEnabled()) {
                    getLog().debug(
                            "Setting property for " + dependencyConflictId
                                    + " with key=" + key + ", path=" + path);
                }
                properties.setProperty(key, path);
            }
        }
    }
}
