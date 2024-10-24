package com.yunying.ai.controller;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.DataType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.dml.DeleteParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/milvus")
public class MilvusController {


    @Autowired
    private MilvusClient milvusClient;


    public String createCollection() {
        CreateCollectionParam quickSetupReq = CreateCollectionParam.newBuilder()
                .withCollectionName("test_collection")
                .build();
        milvusClient.createCollection(quickSetupReq);
        return "Collection created successfully";
    }

    @GetMapping("/createCollection")
    public R<RpcStatus> create() {
        FieldType id = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .withDescription("primary key")
                .build();
        FieldType type_id  = FieldType.newBuilder()
                .withName("type_id")
                .withDataType(DataType.Int64)
                .withDescription("type_id")
                .build();
        FieldType title  = FieldType.newBuilder()
                .withName("title")
                .withDataType(DataType.VarChar)
                .withMaxLength(10000)
                .withDescription("title")
                .build();
        FieldType content  = FieldType.newBuilder()
                .withName("content")
                .withDataType(DataType.VarChar)
                .withMaxLength(10000)
                .withDescription("content")
                .build();
        FieldType title_vector = FieldType.newBuilder()
                .withName("title_vector")
                .withDescription("title_vector")
                .withDataType(DataType.FloatVector)
                .withDimension(1024)
                .build();

        CreateCollectionParam param = CreateCollectionParam.newBuilder()
                .withCollectionName("FAQ")
                .addFieldType(id)
                .addFieldType(type_id)
                .addFieldType(title)
                .addFieldType(content)
                .addFieldType(title_vector)
                .build();

        R<RpcStatus> response = milvusClient.createCollection(param);
        if (response.getStatus() != R.Status.Success.getCode()) {
            System.out.println(response.getMessage());
        }
        return response;
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
