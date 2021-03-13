TITLE GRAPHQL Schema Generator
ECHO Please wait... Generating schema
gradlew :app:downloadApolloSchema -Pcom.apollographql.apollo.endpoint=https://floating-basin-72676.herokuapp.com/query -Pcom.apollographql.apollo.schema=src/main/graphql/com/example/inception/schema.json
PAUSE