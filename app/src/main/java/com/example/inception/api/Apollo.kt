package com.example.inception.api

import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.builder()
    .serverUrl("https://safe-forest-36324.herokuapp.com/graphql")
    .build()