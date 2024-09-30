
package fr.jmini.gql.schema.model;

import java.util.List;

public class Directive implements Descriptable {

    private String name;
    private String description;
    private Boolean isRepeatable;
    private List<String> locations;
    private List<InputValue> args;

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

    public Boolean getIsRepeatable() {
        return isRepeatable;
    }

    public void setIsRepeatable(Boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<InputValue> getArgs() {
        return args;
    }

    public void setArgs(List<InputValue> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Directive [name=" + name + ", description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "Directive [name=" + name + ", description=" + description + ", locations=" + locations + ", args=" + args + "]";
    }
}
