package com.yunying.ai.controller;

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

@RestController
@RequestMapping("/ai")
public class VectorController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmbeddingModel embeddingModel;

//    @GetMapping("/embedding")
//    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(message));
//        return Map.of("embedding", embeddingResponse);
//    }
//
//    @PostMapping("/vector")
//    public List<Document> search(@RequestParam("query") String query) {
//        List<Document> documents = new ArrayList<>();
//        // 示例文本数据
//        String text1 = "Spring AI rocks!!";
//        String text2 = "The World is Big and Salvation Lurks Around the Corner";
//        String text3 = "You walk forward facing the past and you turn back toward the future.";
//
//        // 为每个文本生成向量并创建文档
//        documents.add(new Document(text1, Map.of("meta1", embeddingModel.embedForResponse(List.of(text1)))));
//        documents.add(new Document(text2, Map.of("meta2", embeddingModel.embedForResponse(List.of(text2)))));
//        documents.add(new Document(text3, Map.of("meta3", embeddingModel.embedForResponse(List.of(text3)))));
//
//        // 添加文档到 Milvus
//        vectorStore.add(documents);
//
//        // 执行搜索
//        SearchRequest searchRequest = SearchRequest.query(query).withTopK(5);
//        return vectorStore.similaritySearch(searchRequest);
//    }
}
