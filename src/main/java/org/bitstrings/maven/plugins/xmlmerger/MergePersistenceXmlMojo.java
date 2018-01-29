package org.bitstrings.maven.plugins.xmlmerger;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.atteo.xmlcombiner.XmlCombiner;
import org.bitstrings.maven.plugins.xmlmerger.XmlFileSet.Operation;
import org.bitstrings.maven.plugins.xmlmerger.util.FileSetManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TVFS;

@Mojo( name = "merge", defaultPhase = GENERATE_RESOURCES, threadSafe = true )
public class MergePersistenceXmlMojo
    extends AbstractMojo
{
    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject mavenProject;

    @Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession mavenSession;

    @Parameter( defaultValue = "false" )
    private boolean verbose;

    @Parameter( required = true )
    private String outputFile;

    @Parameter
    private XmlFileSet[] xmlFileSets;

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            XmlCombiner combiner = new XmlCombiner();

            for ( XmlFileSet xmlFileSet : xmlFileSets )
            {
                FileSetManager fileSetManager = new FileSetManager( xmlFileSet );

                List<TFile> files = fileSetManager.resolve();

                if ( getLog().isInfoEnabled() )
                {
                    getLog().info( "Found " + files.size() + " file(s)" );
                }

                for ( TFile file : files )
                {
                    if ( getLog().isInfoEnabled() )
                    {
                        getLog().info( " Processing [ " + file + " ]" );
                    }

                    try ( InputStream in = new TFileInputStream( file ) )
                    {
                        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( in );

                        for ( Operation operation : xmlFileSet.getOperations() )
                        {
                            XPath xPath = XPathFactory.newInstance().newXPath();
                            NodeList nodeList =
                                (NodeList) xPath.compile( operation.getXpath() )
                                    .evaluate( document, XPathConstants.NODESET );

                            for ( int index = 0; index < nodeList.getLength(); index++ )
                            {
                                Element element = (Element) nodeList.item( index );

                                element.setAttribute(
                                    operation.getType().getAttributeName(),
                                    operation.getType().getAttributeValue()
                                );
                            }
                        }

                        combiner.combine( document );

                    }

                    if ( xmlFileSet.isRemoveFiles() )
                    {
                        file.rm();

                        if ( getLog().isInfoEnabled() )
                        {
                            getLog().info( "Removed file [ " + file + " ]" );
                        }
                    }
                }
            }

            try ( OutputStream out = new TFileOutputStream( outputFile ) )
            {
                combiner.buildDocument( out );

                if ( getLog().isInfoEnabled() )
                {
                    getLog().info( "Output file [ " + outputFile + " ]" );
                }
            }
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( e.getLocalizedMessage(), e );
        }
        finally
        {
            try
            {
                TVFS.umount();
            }
            catch ( Exception e )
            {
                throw new MojoFailureException( e.getLocalizedMessage(), e );
            }
        }
    }
}
