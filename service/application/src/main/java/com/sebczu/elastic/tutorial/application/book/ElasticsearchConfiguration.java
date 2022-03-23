package com.sebczu.elastic.tutorial.application.book;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@RequiredArgsConstructor
@EnableReactiveElasticsearchRepositories(basePackages = "com.sebczu")
public class ElasticsearchConfiguration extends AbstractReactiveElasticsearchConfiguration {

  private final ElasticsearchProperties properties;

  @Override
  @Bean
  public ReactiveElasticsearchClient reactiveElasticsearchClient() {
    final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
      .connectedTo(properties.getUris().toArray(String[]::new))
      .withConnectTimeout(properties.getConnectionTimeout())
      .withSocketTimeout(properties.getSocketTimeout())
      .build();
    return ReactiveRestClients.create(clientConfiguration);
  }
}



