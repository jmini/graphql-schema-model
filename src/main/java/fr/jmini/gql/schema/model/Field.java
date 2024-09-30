
package fr.jmini.gql.schema.model;

import java.util.List;

public class Field implements Deprecable {

    private String name;
    private String description;
    private List<InputValue> args;
    private TypeRef type;
    private Boolean isDeprecated;
    private String deprecationReason;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<InputValue> getArgs() {
        return args;
    }

    public void setArgs(List<InputValue> args) {
        this.args = args;
    }

    public TypeRef getType() {
        return type;
    }

    public void setType(TypeRef type) {
        this.type = type;
    }

    @Override
    public Boolean getIsDeprecated() {
        return isDeprecated;
    }

    @Override
    public String toString() {
        return "Field [name=" + name + ", description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "Field [name=" + name + ", description=" + description + ", args=" + args + ", type=" + type + ", isDeprecated=" + isDeprecated + ", deprecationReason=" + deprecationReason + "]";
    }

    public void setIsDeprecated(Boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
    }

    @Override
    public String getDeprecationReason() {
        return deprecationReason;
    }

    public void setDeprecationReason(String deprecationReason) {
        this.deprecationReason = deprecationReason;
    }

}
