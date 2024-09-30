
package fr.jmini.gql.schema.model;

public class EnumValue implements Deprecable {

    private String name;
    private String description;
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

    @Override
    public String toString() {
        return "EnumValue [name=" + name + ", description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "EnumValue [name=" + name + ", description=" + description + ", isDeprecated=" + isDeprecated + ", deprecationReason=" + deprecationReason + "]";
    }
}
