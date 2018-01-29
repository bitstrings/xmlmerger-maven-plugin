package org.bitstrings.maven.plugins.xmlmerger.util;

import java.util.Collections;
import java.util.List;

public class FileSet
{
    protected static List<String> ALL_PATTERN_LIST = Collections.singletonList( "**/*" );

    private String directory;
    private boolean ignoreCase = false;
    private boolean visitArchive = false;
    private List<String> includes = ALL_PATTERN_LIST;
    private List<String> excludes = Collections.EMPTY_LIST;

    public void setDirectory( String directory )
    {
        this.directory = directory;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setIgnoreCase( boolean ignoreCase )
    {
        this.ignoreCase = ignoreCase;
    }

    public boolean isIgnoreCase()
    {
        return ignoreCase;
    }

    public void setIncludes( List<String> includes )
    {
        this.includes = includes;
    }

    public List<String> getIncludes()
    {
        return includes;
    }

    public void setExcludes( List<String> excludes )
    {
        this.excludes = excludes;
    }

    public List<String> getExcludes()
    {
        return excludes;
    }

    public boolean isVisitArchive()
    {
        return visitArchive;
    }

    public void setVisitArchive( boolean visitArchive )
    {
        this.visitArchive = visitArchive;
    }
}
