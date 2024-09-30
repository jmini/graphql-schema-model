
package fr.jmini.gql.schema.model;

import java.util.List;

public class Type implements Descriptable {

    private Kind kind;
    private String name;
    private String description;
    private List<Field> fields;
    private List<InputValue> inputFields;
    private List<TypeRef> interfaces;
    private List<EnumValue> enumValues;
    private List<TypeRef> possibleTypes;
    private String specifiedByURL;

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

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<InputValue> getInputFields() {
        return inputFields;
    }

    public void setInputFields(List<InputValue> inputFields) {
        this.inputFields = inputFields;
    }

    public List<TypeRef> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<TypeRef> interfaces) {
        this.interfaces = interfaces;
    }

    public List<EnumValue> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<EnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public List<TypeRef> getPossibleTypes() {
        return possibleTypes;
    }

    public void setPossibleTypes(List<TypeRef> possibleTypes) {
        this.possibleTypes = possibleTypes;
    }

    public String getSpecifiedByURL() {
        return specifiedByURL;
    }

    public void setSpecifiedByURL(String specifiedByURL) {
        this.specifiedByURL = specifiedByURL;
    }

    @Override
    public String toString() {
        return "Type [kind=" + kind + ", name=" + name + ", description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "Type [kind=" + kind + ", name=" + name + ", description=" + description + ", fields=" + fields + ", inputFields=" + inputFields + ", interfaces=" + interfaces + ", enumValues=" + enumValues + ", possibleTypes="
                + possibleTypes + ", specifiedByURL=" + specifiedByURL + "]";
    }

}
