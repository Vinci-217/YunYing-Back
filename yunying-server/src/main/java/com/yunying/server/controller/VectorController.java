package com.yunying.server.controller;

import org.springframework.ai.document.Document;
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

    // 假设这是一个生成向量的方法，您需要替换为实际的向量生成逻辑
    private float[] generateVector(String text) {
        float[] vector = new float[1536];
        // 这里应该是将文本转换为向量的实际逻辑
        // 例如，使用预训练的模型来获取文本的嵌入向量
        return vector;
    }

    @PostMapping("/vector")
    public List<Document> search(@RequestBody String query) {
        List<Document> documents = new ArrayList<>();
        // 示例文本数据
        String text1 = "Spring AI rocks!!";
        String text2 = "The World is Big and Salvation Lurks Around the Corner";
        String text3 = "You walk forward facing the past and you turn back toward the future.";

        // 为每个文本生成向量并创建文档
        documents.add(new Document(text1, Map.of("meta1", "meta1")));
        documents.add(new Document(text2, Map.of("meta2", "meta2")));
        documents.add(new Document(text3, Map.of("meta3", "meta3")));

        // 添加文档到 Milvus
        vectorStore.add(documents);

        // 执行搜索
        SearchRequest searchRequest = SearchRequest.query(query).withTopK(5);
        return vectorStore.similaritySearch(searchRequest);
    }
}
