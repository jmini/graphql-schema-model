package fr.jmini.gql.schema.model;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.jmini.gql.schema.SchemaUtil;

public class IntegrationTest {

    private static final ObjectMapper MAPPER = createMapper();

    @Test
    void testGithub() throws Exception {
        Path file = Paths.get("src/test/resources/github-graphqlschema.json");
        Schema schema = runTest(file);
        Type queryType = SchemaUtil.getQueryType(schema);
        Type mutationType = SchemaUtil.getMutationType(schema);
        Type subscriptionType = SchemaUtil.getSubscriptionType(schema);
        assertThat(queryType).isNotNull();
        assertThat(mutationType).isNotNull();
        assertThat(subscriptionType).isNull();
        Map<IntrospectionMember, List<IntrospectionMember>> allElements = SchemaUtil.getAllElements(schema);
        assertThat(allElements).hasSize(28631);
        assertThat(allElements).containsEntry(schema, Collections.singletonList(schema));
        assertThat(allElements).containsEntry(queryType, Arrays.asList(schema, queryType));
        assertThat(allElements).containsEntry(mutationType, Arrays.asList(schema, mutationType));
        verifyPathAsString(allElements);
    }

    @Test
    void testGitLab() throws Exception {
        Path file = Paths.get("src/test/resources/gitlab-graphqlschema.json");
        Schema schema = runTest(file);
        Type queryType = SchemaUtil.getQueryType(schema);
        Type mutationType = SchemaUtil.getMutationType(schema);
        Type subscriptionType = SchemaUtil.getSubscriptionType(schema);
        assertThat(queryType).isNotNull();
        assertThat(mutationType).isNotNull();
        assertThat(subscriptionType).isNotNull();
        Map<IntrospectionMember, List<IntrospectionMember>> allElements = SchemaUtil.getAllElements(schema);
        assertThat(allElements).hasSize(44764);
        assertThat(allElements).containsEntry(schema, Collections.singletonList(schema));
        assertThat(allElements).containsEntry(queryType, Arrays.asList(schema, queryType));
        assertThat(allElements).containsEntry(mutationType, Arrays.asList(schema, mutationType));
        assertThat(allElements).containsEntry(subscriptionType, Arrays.asList(schema, subscriptionType));
        verifyPathAsString(allElements);
    }

    @Test
    void testSuperheroes() throws Exception {
        Path file = Paths.get("src/test/resources/superheroes-graphqlschema.json");
        Schema schema = runTest(file);
        Type queryType = SchemaUtil.getQueryType(schema);
        Type mutationType = SchemaUtil.getMutationType(schema);
        Type subscriptionType = SchemaUtil.getSubscriptionType(schema);
        assertThat(queryType).isNotNull();
        assertThat(mutationType).isNotNull();
        assertThat(subscriptionType).isNull();
        TypeRef ref = new TypeRef();
        ref.setKind(Kind.OBJECT);
        ref.setName("City");
        Type typeByRef = SchemaUtil.getTypeByRef(schema, ref);
        assertThat(typeByRef).isNotNull();
        Field allCities = SchemaUtil.getFieldByName(schema, queryType, "allCities");
        assertThat(allCities).isNotNull();
        Type typeByRef2 = SchemaUtil.getTypeByRef(schema, allCities.getType());
        assertThat(typeByRef2).isSameAs(typeByRef);

        Type cityInput = SchemaUtil.getTypeByKindAndName(schema, Kind.INPUT_OBJECT, "CityInput");
        assertThat(cityInput).isNotNull();
        InputValue name = SchemaUtil.getInputFieldByName(schema, cityInput, "name");
        assertThat(name).isNotNull();

        Map<IntrospectionMember, List<IntrospectionMember>> allElements = SchemaUtil.getAllElements(schema);
        assertThat(allElements).hasSize(272);
        assertThat(allElements).containsEntry(schema, Collections.singletonList(schema));
        assertThat(allElements).containsKey(typeByRef);
        assertThat(allElements.get(typeByRef)).containsExactly(schema, typeByRef);
        assertThat(allElements).containsKey(allCities);
        assertThat(allElements.get(allCities)).containsExactly(schema, queryType, allCities);

        assertThat(SchemaUtil.getPathAsString(allElements.get(typeByRef), "Superheroes", ".")).isEqualTo("Superheroes.City");
        assertThat(SchemaUtil.getPathAsString(allElements.get(allCities), "SCHEMA", ".")).isEqualTo("SCHEMA.Query.allCities");
        verifyPathAsString(allElements);
    }

    @Test
    void testSwapi() throws Exception {
        Path file = Paths.get("src/test/resources/swapi-graphqlschema.json");
        Schema schema = runTest(file);
        Type queryType = SchemaUtil.getQueryType(schema);
        Type mutationType = SchemaUtil.getMutationType(schema);
        Type subscriptionType = SchemaUtil.getSubscriptionType(schema);
        assertThat(queryType).isNotNull();
        assertThat(mutationType).isNull();
        assertThat(subscriptionType).isNull();
        Map<IntrospectionMember, List<IntrospectionMember>> allElements = SchemaUtil.getAllElements(schema);
        assertThat(allElements).hasSize(1015);
        assertThat(allElements).containsEntry(schema, Collections.singletonList(schema));
        assertThat(allElements).containsEntry(queryType, Arrays.asList(schema, queryType));
        verifyPathAsString(allElements);
    }

    private Schema runTest(Path file) throws IOException, JsonProcessingException, JsonMappingException {
        String originalContent = readString(file);

        IntrospectionResponse object = MAPPER.readValue(originalContent, IntrospectionResponse.class);
        String newContent = MAPPER.writeValueAsString(object);

        assertThatJson(newContent).isEqualTo(originalContent);
        return object.getSchema();
    }

    private void verifyPathAsString(Map<IntrospectionMember, List<IntrospectionMember>> allElements) {
        List<String> paths = allElements.values()
                .stream()
                .map(p -> SchemaUtil.getPathAsString(p, "Root", "."))
                .collect(Collectors.toList());
        assertThat(paths)
                .doesNotHaveDuplicates()
                .allSatisfy(s -> assertThat(s).doesNotContain("null"));
    }

    private String readString(Path file) throws IOException {
        byte[] b = Files.readAllBytes(file);
        return new String(b, StandardCharsets.UTF_8);
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return mapper;
    }
}
