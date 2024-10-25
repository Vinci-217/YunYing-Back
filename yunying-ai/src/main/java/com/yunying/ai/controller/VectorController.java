package com.yunying.ai.controller;

import com.yunying.common.utils.Result;
import io.milvus.client.MilvusClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.MilvusVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai/vector")
public class VectorController {

    @Autowired
    private MilvusVectorStore vectorStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @PostMapping("/insert")
    public Result insert() {
        List <Document> documents = List.of(
//                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
//                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("原神是一款五星级游戏.", Map.of("meta2", "meta2")));
        vectorStore.add(documents);
        return Result.success();
    }

    @PostMapping("/select")
    public Result search(@RequestParam("query") String query) {
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query("Spring").withTopK(2));
        System.out.println(results);
        return Result.success(results);
    }
}
