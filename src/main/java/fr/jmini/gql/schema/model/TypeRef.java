
package fr.jmini.gql.schema.model;

public class TypeRef implements Namable {

    private Kind kind;
    private String name;
    private TypeRef ofType;

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeRef getOfType() {
        return ofType;
    }

    public void setOfType(TypeRef ofType) {
        this.ofType = ofType;
    }

    @Override
    public String toString() {
        return "TypeRef [kind=" + kind + ", name=" + name + "]";
    }

    @Override
    public String toStringContent() {
        return "TypeRef [kind=" + kind + ", name=" + name + ", ofType=" + ofType + "]";
    }
}
