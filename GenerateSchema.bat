TITLE GRAPHQL Schema Generator
ECHO Please wait... Generating schema
gradlew :app:downloadApolloSchema -Pcom.apollographql.apollo.endpoint=https://safe-forest-36324.herokuapp.com/graphql -Pcom.apollographql.apollo.schema=src/main/graphql/com/example/inception/schema.json
PAUSE