
package fr.jmini.gql.schema.model;

public class IntrospectionResponse {

    private Schema __schema;

    public Schema getSchema() {
        return __schema;
    }

    public void setSchema(Schema schema) {
        this.__schema = schema;
    }

    @Override
    public String toString() {
        return "IntrospectionResponse [__schema=" + __schema + "]";
    }

}
