
package fr.jmini.gql.schema.model;

public class MutationType implements Namable {

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
        return "MutationType [name=" + name + "]";
    }
}
