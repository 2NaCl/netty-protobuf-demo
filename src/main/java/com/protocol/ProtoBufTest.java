package com.protocol;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {

        //先序列化
        DataInfo.Student build = DataInfo.Student.newBuilder()
                .setName("zhangsan")
                .setAddress("Beijing")
                .setAge(18)
                .build();
        //序列化之后存储字节数组
        byte[] student2ByteArray = build.toByteArray();

        //反序列化回来
        DataInfo.Student student = DataInfo.Student.parseFrom(student2ByteArray);

        System.out.println(student);

    }
}
