package com.yunying.ai.controller;

import io.milvus.client.MilvusClient;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.dml.DeleteParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/milvus")
public class MilvusController {


    @Autowired
    private MilvusClient milvusClient;


    @GetMapping("/createCollection")
    public String createCollection() {
        CreateCollectionParam quickSetupReq = CreateCollectionParam.newBuilder()
                .withCollectionName("test_collection")
                .build();
        milvusClient.createCollection(quickSetupReq);
        return "Collection created successfully";
    }

//    @GetMapping("/insertData")
//    public String insertData(@RequestParam String collectionName, @RequestParam List<Float> vectorData) {
//        try {
//            List<Float> vector = vectorData;
//            R<List<Long>> response = milvusClient.collectionManager().insert(new InsertParam.Builder(collectionName)
//                    .withFloatVectorField("vector_field", vector)
//                    .build());
//            return "Data inserted successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to insert data: " + e.getMessage();
//        }
//    }

//    @GetMapping("/queryData")
//    public String queryData(@RequestParam String collectionName, @RequestParam List<Float> vectorData) {
//        try {
//            List<Float> vector = vectorData;
//            R<List<Long>> response = milvusClient.collectionManager().search(new SearchParam.Builder(collectionName)
//                    .withDSL("vector_field in [[" + String.join(" ", vector) + "]]")
//                    .build());
//            return "Data queried successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to query data: " + e.getMessage();
//        }
//    }
//
//    @GetMapping("/deleteData")
//    public String deleteData(@RequestParam String collectionName, @RequestParam Long id) {
//        try {
//            R<Boolean> response = milvusClient.collectionManager().deleteEntity(new DeleteParam.Builder(collectionName)
//                    .withIds(id)
//                    .build());
//            return "Data deleted successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to delete data: " + e.getMessage();
//        }
//    }
//
//    @GetMapping("/updateData")
//    public String updateData(@RequestParam String collectionName, @RequestParam Long id, @RequestParam List<Float> vectorData) {
//        try {
//            List<Float> vector = vectorData;
//            R<Boolean> response = milvusClient.collectionManager().updateEntity(new UpdateParam.Builder(collectionName)
//                    .withIds(id)
//                    .withFloatVectorField("vector_field", vector)
//                    .build());
//            return "Data updated successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to update data: " + e.getMessage();
//        }

}
