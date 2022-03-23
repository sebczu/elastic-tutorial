package com.sebczu.elastic.tutorial.application.book;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import static com.sebczu.elastic.tutorial.application.book.ElasticsearchInitialization.BOOK_INDEX;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(indexName = BOOK_INDEX, createIndex = true)
@Setting(
  shards = 2,
  replicas = 2)
public class Book {

  @Id
  private String id;

  @Field(type = FieldType.Keyword)
  private String title;

  @Field(type = FieldType.Text)
  private String content;

}

