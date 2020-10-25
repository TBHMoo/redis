package com.xli.redis.delayqueue;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.lang.reflect.Type;

public class RedisDelayQueue<T> {
    static class TaskItem<T>{
        public String id;
        public T msg;
    }


}
