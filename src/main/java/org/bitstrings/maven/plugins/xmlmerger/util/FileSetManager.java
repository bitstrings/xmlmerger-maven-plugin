package org.bitstrings.maven.plugins.xmlmerger.util;

import java.util.Collections;
import java.util.List;

import net.java.truevfs.access.TFile;

public class FileSetManager
{
    private final FileSet fileSet;

    public FileSetManager( FileSet fileSet )
    {
        this.fileSet = fileSet;
    }

    public FileSet getFileSet()
    {
        return fileSet;
    }

    public List<TFile> resolve()
    {
        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setBasedir( new TFile( fileSet.getDirectory() ) );
        scanner.setVisitArchive( fileSet.isVisitArchive() );
        scanner.setCaseSensitive( !fileSet.isIgnoreCase() );
        scanner.setIncludes( fileSet.getIncludes().toArray( new String[ fileSet.getIncludes().size() ] ) );

        scanner.scan();

        return scanner.getIncludedTFiles();
    }

    public static void main(String[] args)
    {
        FileSet fileSet = new FileSet();

        fileSet.setDirectory( "/home/p/tmp/xx/" );
        fileSet.setIncludes( Collections.singletonList( "docker-17.12.0-ce.tgz/**/*shim" ) );

        FileSetManager fileSetManager = new FileSetManager( fileSet );

        System.out.println( "TFile s: " + fileSetManager.resolve() );
    }
}
