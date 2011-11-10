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
    private Selector[] selectors;

    @MojoParameter
    private String suffixDefault;

    @MojoParameter
    private File relativeToDefault;

    @MojoParameter(defaultValue="true")
    private boolean transitiveDefault;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final Properties properties = project.getProperties();

        if (selectors == null)
        {
            selectors = new Selector[] { new Selector() };
        }

        for (Selector selector : selectors)
        {
            final Set<String> includes = selector.getIncludes();
            final Set<String> excludes = selector.getExcludes();

            Boolean transitive = selector.isTransitive();

            if (transitive == null)
            {
                transitive = transitiveDefault;
            }

            final Set<Artifact> artifacts =
                            transitive
                                    ? project.getArtifacts()
                                    : project.getDependencyArtifacts();

            for (Artifact artifact : artifacts)
            {
                final String dependencyConflictId = artifact.getDependencyConflictId();

                if ((includes != null) && !includes.contains(dependencyConflictId))
                {
                    continue;
                }

                if ((excludes != null) && excludes.contains(dependencyConflictId))
                {
                    continue;
                }

                String key = dependencyConflictId;

                File relativeTo = selector.getRelativeTo();

                if (relativeTo == null)
                {
                    relativeTo = this.relativeToDefault;
                }

                if (relativeTo != null)
                {
                    key += ".relative";
                }

                String suffix = selector.getSuffix();

                if (suffix == null)
                {
                    suffix = this.suffixDefault;
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
