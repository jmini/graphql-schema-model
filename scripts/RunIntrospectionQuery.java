///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.6.3
//DEPS io.smallrye:smallrye-graphql-client-implementation-vertx:2.10.0
//DEPS org.eclipse:yasson:2.0.4

//JAVA 17

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClientBuilder;
import io.smallrye.graphql.client.vertx.dynamic.VertxDynamicGraphQLClientBuilder;
import io.vertx.core.Vertx;
import jakarta.json.Json;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "RunIntrospectionQuery", mixinStandardHelpOptions = true, version = "RunIntrospectionQuery 0.1", description = "run GraphQL Introspection query")
class RunIntrospectionQuery implements Callable<Integer> {

    @Parameters(index = "0", description = "GraphQL server")
    private String url;

    @Option(names = { "-h", "--header" }, description = "File where to store the result")
    private Map<String, String> headers;

    @Option(names = { "-q", "--query" }, description = "File where containing the query")
    private Path queryFile;

    @Option(names = { "--printQuery" }, description = "Print the query")
    private boolean printQuery;

    @Option(names = { "-o", "--output" }, description = "File where to store the result")
    private Path outputFile;

    @Option(names = { "-p", "--print" }, description = "Print the result")
    private boolean printResponse;

    @Option(names = { "-f", "--pretty" }, description = "Format the response as pretty JSON")
    private boolean pretty;

    @Override
    public Integer call() throws Exception {
        if (!printResponse && outputFile == null) {
            System.err.println("Nothing to do with the query result, set '--print' or '--output'");
            return 1;
        }

        Vertx vertx = Vertx.vertx();
        DynamicGraphQLClientBuilder builder = new VertxDynamicGraphQLClientBuilder()
                .url(url)
                .vertx(vertx);
        if (headers != null) {
            headers.forEach((k, v) -> {
                builder.header(k, v);
            });
        }
        DynamicGraphQLClient client = builder.build();

        String query;
        if (queryFile != null) {
            query = Files.readString(queryFile);
        } else {
            query = defaultQuery();
        }

        if (printQuery) {
            System.out.println("===== QUERY =====");
            System.out.println(query);
            System.out.println("===== END OF QUERY =====");
        }

        Response response = client.executeSync(query);

        String stringResponse = toString(response);
        if (outputFile != null) {
            Files.writeString(outputFile, stringResponse);
        }
        if (printResponse) {
            System.out.println(stringResponse);
        } else {
            System.out.println("DONE");
        }

        return 0;
    }

    private String toString(Response response) {
        if (pretty) {
            Map<String, Boolean> config = Map.of(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory jwf = Json.createWriterFactory(config);
            StringWriter sw = new StringWriter();
            try (JsonWriter jsonWriter = jwf.createWriter(sw)) {
                jsonWriter.writeObject(response.getData());
            }
            return sw.toString();
        } else {
            return response.toString();
        }
    }

    private static String defaultQuery() {
        return """
                query IntrospectionQuery {
                  __schema {
                    queryType {
                      name
                    }
                    mutationType {
                      name
                    }
                    subscriptionType {
                      name
                    }
                    types {
                      ...FullType
                    }
                    directives {
                      name
                      description
                      locations
                      args {
                        ...InputValue
                      }
                    }
                  }
                }

                fragment FullType on __Type {
                  kind
                  name
                  description
                  fields(includeDeprecated: true) {
                    name
                    description
                    args {
                      ...InputValue
                    }
                    type {
                      ...TypeRef
                    }
                    isDeprecated
                    deprecationReason
                  }
                  inputFields {
                    ...InputValue
                  }
                  interfaces {
                    ...TypeRef
                  }
                  enumValues(includeDeprecated: true) {
                    name
                    description
                    isDeprecated
                    deprecationReason
                  }
                  possibleTypes {
                    ...TypeRef
                  }
                }

                fragment InputValue on __InputValue {
                  name
                  description
                  type {
                    ...TypeRef
                  }
                  defaultValue
                }

                fragment TypeRef on __Type {
                  kind
                  name
                  ofType {
                    kind
                    name
                    ofType {
                      kind
                      name
                      ofType {
                        kind
                        name
                        ofType {
                          kind
                          name
                          ofType {
                            kind
                            name
                            ofType {
                              kind
                              name
                              ofType {
                                kind
                                name
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
                """;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new RunIntrospectionQuery()).execute(args);
        System.exit(exitCode);
    }
}
