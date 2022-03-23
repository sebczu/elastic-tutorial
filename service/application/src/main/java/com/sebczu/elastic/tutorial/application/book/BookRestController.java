package com.sebczu.elastic.tutorial.application.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.sebczu.elastic.tutorial.application.book.ElasticsearchInitialization.BOOK_INDEX;
import static java.util.Collections.singletonMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookRestController {

  private final BookRepository repository;
  private final ReactiveElasticsearchClient client;

  @GetMapping
  public Flux<Book> list() {
    return repository.findAll();
  }

  @PostMapping
  public Mono<Book> save() {
    Book book = new Book();
    book.setTitle("new book");
    return repository.save(book);
  }

  @PostMapping("/index")
  public Mono<Boolean> createIndex() {
    Settings settings = Settings.builder()
      .put("index.number_of_shards", "2")
      .put("index.number_of_replicas", "2")
      .build();

    CreateIndexRequest request = new CreateIndexRequest(BOOK_INDEX)
      .settings(settings)
      .source(singletonMap("feature", "reactive-client"));

    return client.indices()
      .createIndex(request)
      .doOnError(error -> log.error("error", error));
  }

  @GetMapping("/index")
  public Mono<GetIndexResponse> getIndex() {
    return client.indices()
      .getIndex(new GetIndexRequest(BOOK_INDEX))
      .doOnError(error -> log.error("error", error));
  }

  @DeleteMapping("/index")
  public Mono<Boolean> deleteIndex() {
    return client.indices()
      .deleteIndex(new DeleteIndexRequest(BOOK_INDEX))
      .doOnError(error -> log.error("error", error));
  }

}
