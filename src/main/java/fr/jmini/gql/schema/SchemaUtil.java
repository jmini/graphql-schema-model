package fr.jmini.gql.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.jmini.gql.schema.model.Directive;
import fr.jmini.gql.schema.model.EnumValue;
import fr.jmini.gql.schema.model.Field;
import fr.jmini.gql.schema.model.InputValue;
import fr.jmini.gql.schema.model.IntrospectionMember;
import fr.jmini.gql.schema.model.Kind;
import fr.jmini.gql.schema.model.MutationType;
import fr.jmini.gql.schema.model.Namable;
import fr.jmini.gql.schema.model.QueryType;
import fr.jmini.gql.schema.model.Schema;
import fr.jmini.gql.schema.model.SubscriptionType;
import fr.jmini.gql.schema.model.Type;
import fr.jmini.gql.schema.model.TypeRef;

public class SchemaUtil {

    public static Type getQueryType(Schema schema) {
        QueryType type = schema.getQueryType();
        if (type == null) {
            return null;
        }
        String typeName = type.getName();
        return getTypeByName(schema, typeName);
    }

    public static Type getMutationType(Schema schema) {
        MutationType type = schema.getMutationType();
        if (type == null) {
            return null;
        }
        String typeName = type.getName();
        return getTypeByName(schema, typeName);
    }

    public static Type getSubscriptionType(Schema schema) {
        SubscriptionType type = schema.getSubscriptionType();
        if (type == null) {
            return null;
        }
        String typeName = type.getName();
        return getTypeByName(schema, typeName);
    }

    public static Type getTypeByRef(Schema schema, TypeRef ref) {
        if (ref == null) {
            return null;
        }
        String typeName = ref.getName();
        if (typeName != null) {
            return getTypeByKindAndName(schema, ref.getKind(), typeName);
        }
        TypeRef ofType = ref.getOfType();
        if (ofType != null) {
            return getTypeByRef(schema, ofType);
        }
        throw new IllegalStateException("Could not find type by ref " + ref);
    }

    public static Type getTypeByName(Schema schema, String typeName) {
        if (typeName == null) {
            return null;
        }
        return schema.getTypes()
                .stream()
                .filter(t -> typeName.equals(t.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find type with name '" + typeName + "'"));
    }

    public static Type getTypeByKindAndName(Schema schema, Kind typeKind, String typeName) {
        if (typeName == null) {
            return null;
        }
        return schema.getTypes()
                .stream()
                .filter(t -> matchesNameAndRef(t, typeKind, typeName))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find type with kind '" + typeKind + "' and name '" + typeName + "'"));
    }

    public static boolean matchesTypeRef(Type type, TypeRef typeRef) {
        return matchesNameAndRef(type, typeRef.getKind(), typeRef.getName());
    }

    public static boolean matchesNameAndRef(Type type, Kind typeKind, String typeName) {
        return type.getKind() == typeKind
                && Objects.equals(type.getName(), typeName);
    }

    public static Field getFieldByName(Schema schema, Type type, String fieldName) {
        if (fieldName == null) {
            return null;
        }
        List<Field> fields = type.getFields();
        return fields.stream()
                .filter(f -> fieldName.equals(f.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find field with name '" + fieldName + "' in type: " + type));
    }

    public static InputValue getInputFieldByName(Schema schema, Type type, String inputFieldName) {
        if (inputFieldName == null) {
            return null;
        }
        List<InputValue> inputFields = type.getInputFields();
        return inputFields.stream()
                .filter(f -> inputFieldName.equals(f.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find inputField with name '" + inputFieldName + "' in type: " + type));
    }

    public static Map<IntrospectionMember, List<IntrospectionMember>> getAllElements(Schema schema) {
        Map<IntrospectionMember, List<IntrospectionMember>> result = new LinkedHashMap<>();
        List<IntrospectionMember> currentPath = Collections.singletonList(schema);
        result.put(schema, currentPath);
        if (schema.getQueryType() != null) {
            addElements(result, currentPath, schema.getQueryType());
        }
        if (schema.getMutationType() != null) {
            addElements(result, currentPath, schema.getMutationType());
        }
        if (schema.getSubscriptionType() != null) {
            addElements(result, currentPath, schema.getSubscriptionType());
        }
        if (schema.getTypes() != null) {
            for (Type e : schema.getTypes()) {
                addElements(result, currentPath, e);
            }
        }
        if (schema.getDirectives() != null) {
            for (Directive e : schema.getDirectives()) {
                addElements(result, currentPath, e);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, Type item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
        if (item.getFields() != null) {
            for (Field e : item.getFields()) {
                addElements(result, currentPath, e);
            }
        }
        if (item.getInputFields() != null) {
            for (InputValue e : item.getInputFields()) {
                addElements(result, currentPath, e);
            }
        }
        if (item.getInterfaces() != null) {
            for (TypeRef e : item.getInterfaces()) {
                addElements(result, currentPath, e);
            }
        }
        if (item.getEnumValues() != null) {
            for (EnumValue e : item.getEnumValues()) {
                addElements(result, currentPath, e);
            }
        }
        if (item.getPossibleTypes() != null) {
            for (TypeRef e : item.getPossibleTypes()) {
                addElements(result, currentPath, e);
            }
        }
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, Field item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
        if (item.getArgs() != null) {
            for (InputValue e : item.getArgs()) {
                addElements(result, currentPath, e);
            }
        }
        if (item.getType() != null) {
            TypeRef e = item.getType();
            addElements(result, currentPath, e);
        }
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, Directive item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
        if (item.getArgs() != null) {
            for (InputValue e : item.getArgs()) {
                addElements(result, currentPath, e);
            }
        }
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, InputValue item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
        if (item.getType() != null) {
            TypeRef e = item.getType();
            addElements(result, currentPath, e);
        }
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, TypeRef item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
        if (item.getOfType() != null) {
            TypeRef e = item.getOfType();
            addElements(result, currentPath, e);
        }
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, EnumValue item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, QueryType item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, MutationType item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
    }

    private static void addElements(Map<IntrospectionMember, List<IntrospectionMember>> result, List<IntrospectionMember> path, SubscriptionType item) {
        List<IntrospectionMember> currentPath = addItemToPath(path, item);
        result.put(item, currentPath);
    }

    private static List<IntrospectionMember> addItemToPath(List<IntrospectionMember> path, IntrospectionMember item) {
        List<IntrospectionMember> newPath = new ArrayList<>();
        newPath.addAll(path);
        newPath.add(item);
        return Collections.unmodifiableList(newPath);
    }

    public static String getPathAsString(List<IntrospectionMember> path, String rootName, String separator) {
        return path.stream()
                .map(item -> {
                    if (item instanceof Schema) {
                        return rootName;
                    } else if (item instanceof TypeRef) {
                        String name = ((TypeRef) item).getName();
                        if (name != null) {
                            return name;
                        } else {
                            return ((TypeRef) item).getKind()
                                    .name();
                        }
                    } else if (item instanceof QueryType) {
                        return item.getClass()
                                .getSimpleName() + separator + ((Namable) item).getName();
                    } else if (item instanceof MutationType) {
                        return item.getClass()
                                .getSimpleName() + separator + ((Namable) item).getName();
                    } else if (item instanceof SubscriptionType) {
                        return item.getClass()
                                .getSimpleName() + separator + ((Namable) item).getName();
                    } else if (item instanceof Namable) {
                        return ((Namable) item).getName();
                    } else {
                        throw new IllegalStateException("Unexpected type in the path: " + item);
                    }
                })
                .collect(Collectors.joining(separator));
    }

}
