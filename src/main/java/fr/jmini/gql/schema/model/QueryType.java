
package fr.jmini.gql.schema.model;

public class QueryType implements Namable {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return toStringContent();
    }

    @Override
    public String toStringContent() {
        return "QueryType [name=" + name + "]";
    }
}
