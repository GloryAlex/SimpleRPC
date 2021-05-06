package com.glory.coder;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

public class Serializers {
    private static final Objenesis objenesis = new ObjenesisStd(true);

    public static <T> Schema<T> getSchema(Class<T> clazz){
        return RuntimeSchema.getSchema(clazz);
    }

    /**
     * 序列化对象
     * @param obj 要序列化的对象
     * @param <T> 对象的类型
     * @return 字节数组
     */
    public static <T> byte[] serialize(T obj){
        Class<T> objClass = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(objClass);

        return ProtostuffIOUtil.toByteArray(obj,schema,buffer);
    }

    /**
     * 反序列化对象
     * @param data 字节数组
     * @param clazz 反序列化类型
     * @param <T> 类型
     * @return 对象
     */
    public static <T> T deserialize(byte[] data,Class<T> clazz){
        T message = (T) objenesis.newInstance(clazz);
        Schema<T> schema = getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(data,message,schema);
        return message;
    }
}
