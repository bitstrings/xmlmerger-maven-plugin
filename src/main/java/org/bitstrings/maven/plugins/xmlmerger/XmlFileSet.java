package org.bitstrings.maven.plugins.xmlmerger;

import java.util.Collections;
import java.util.List;

import org.bitstrings.maven.plugins.xmlmerger.util.FileSet;

public class XmlFileSet
    extends FileSet
{
    public static class Operation
    {
        public enum Type
        {
            MERGE_CHILDREN( "combine.children", "merge" ),
            APPEND_CHILDREN( "combine.children", "append" ),
            REMOVE( "combine.self", "remove" );

            private final String attributeName;
            private final String attributeValue;

            Type( String attributeName, String attributeValue )
            {
                this.attributeName = attributeName;
                this.attributeValue = attributeValue;
            }

            public String getAttributeName()
            {
                return attributeName;
            }

            public String getAttributeValue()
            {
                return attributeValue;
            }
        }

        private String xpath;
        private Type type;

        public String getXpath()
        {
            return xpath;
        }

        public void setXpath( String xpath )
        {
            this.xpath = xpath;
        }

        public Type getType()
        {
            return type;
        }

        public void setType( Type type )
        {
            this.type = type;
        }
    }

    private List<Operation> operations = Collections.EMPTY_LIST;
    private boolean removeFiles = false;

    public List<Operation> getOperations()
    {
        return operations;
    }

    public void setOperations( List<Operation> operations )
    {
        this.operations = operations;
    }

    public boolean isRemoveFiles()
    {
        return removeFiles;
    }

    public void setRemoveFiles( boolean removeFiles )
    {
        this.removeFiles = removeFiles;
    }
}
