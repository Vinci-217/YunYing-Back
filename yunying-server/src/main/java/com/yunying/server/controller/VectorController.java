package com.yunying.server.controller;


import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vector")
public class VectorController {

    @Autowired
    VectorStore vectorStore;




    @PostMapping("/search")
    public List<Document> search() {
        List <Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

        vectorStore.add(documents);

        // Retrieve documents similar to a query
//        List<Document> results = vectorStore.similaritySearch();

        return vectorStore.similaritySearch(SearchRequest.query("Spring").withTopK(5));
    }
}
