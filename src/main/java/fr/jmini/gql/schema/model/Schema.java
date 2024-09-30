
package fr.jmini.gql.schema.model;

import java.util.List;

public class Schema implements IntrospectionMember {

    private String description;
    private QueryType queryType;
    private MutationType mutationType;
    private SubscriptionType subscriptionType;
    private List<Type> types;
    private List<Directive> directives;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public MutationType getMutationType() {
        return mutationType;
    }

    public void setMutationType(MutationType mutationType) {
        this.mutationType = mutationType;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    public String toString() {
        return "Schema [description=" + description + "]";
    }

    @Override
    public String toStringContent() {
        return "Schema [description=" + description + ", queryType=" + queryType + ", mutationType=" + mutationType + ", subscriptionType=" + subscriptionType + ", types=" + types + ", directives=" + directives + "]";
    }
}
