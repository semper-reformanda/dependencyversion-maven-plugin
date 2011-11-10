package org.bitstrings.maven.plugins.dependencypath;

import java.io.File;
import java.util.Set;

import org.jfrog.maven.annomojo.annotations.MojoParameter;

public class Selector
{
    @MojoParameter
    private String suffix;

    @MojoParameter
    private Set<String> includes;

    @MojoParameter
    private Set<String> excludes;

    @MojoParameter
    private File relativeTo;

    @MojoParameter
    private Boolean transitive;

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

    public Boolean isTransitive()
    {
        return transitive;
    }

    public void setTransitive(Boolean transitive)
    {
        this.transitive = transitive;
    }
}
