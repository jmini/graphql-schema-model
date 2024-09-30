
package fr.jmini.gql.schema.model;

public class InputValue implements Deprecable {

    private String name;
    private String description;
    private Boolean isDeprecated;
    private String deprecationReason;
    private TypeRef type;
    private String defaultValue;

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

    @Override
    public Boolean getIsDeprecated() {
        return isDeprecated;
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

    public TypeRef getType() {
        return type;
    }

    public void setType(TypeRef type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "InputValue [name=" + name + ", description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "InputValue [name=" + name + ", description=" + description + ", type=" + type + ", defaultValue=" + defaultValue + "]";
    }
}
