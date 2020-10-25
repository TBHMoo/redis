package com.xli.redis.delayqueue;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

public class RedisDelayQueue<T> {
    static class TaskItem<T>{
        public String id;
        public T msg;
    }

    private Type TaskType = new TypeReference<TaskItem<T>>(){}.getType();

    private RedisTemplate redisTemplate;
    private String delayQueueKey;

    public RedisDelayQueue(RedisTemplate redisTemplate, String delayQueueKey) {
        this.redisTemplate = redisTemplate;
        this.delayQueueKey = delayQueueKey;
    }

    public void delay(T msg,int seconds){
        TaskItem<T> taskItem = new TaskItem<>();
//        分配唯一的UUID
        taskItem.id = UUID.randomUUID().toString();
        taskItem.msg = msg;
//        fastjson 序列化
        String member = JSON.toJSONString(taskItem);
//        塞入延时队列 当前时间+ score 秒
        redisTemplate.opsForZSet().add(delayQueueKey,member,System.currentTimeMillis()+(seconds*1000));
    }

    public void loop(){
        while (!Thread.interrupted()){

            Set<String> set = redisTemplate.opsForZSet().rangeByScore(delayQueueKey, 0, System.currentTimeMillis(), 0, 1);
            if(set.isEmpty()){
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                continue;
            }
            String value = set.iterator().next();
            if(redisTemplate.opsForZSet().remove(delayQueueKey,value) > 0){
                TaskItem<T> taskItem = JSON.parseObject(value,TaskType);
                this.handleMsg(taskItem);
            }
        }
    }

    public void  handleMsg(TaskItem<T> taskItem){
        System.out.println(JSON.toJSON(taskItem));
    }


}
