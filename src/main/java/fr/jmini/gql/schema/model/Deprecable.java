package fr.jmini.gql.schema.model;

public interface Deprecable extends Descriptable {

    Boolean getIsDeprecated();

    String getDeprecationReason();

}
