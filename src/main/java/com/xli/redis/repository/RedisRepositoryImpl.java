package com.xli.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class RedisRepositoryImpl implements RedisRepository{

    @Autowired
    private RedisTemplate redisTemplate;



    @PostConstruct
    public void init(){
    RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
     redisTemplate.setKeySerializer(redisSerializer);
     redisTemplate.setHashKeySerializer(redisSerializer);
    }


    @Override
    public String get(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean setIfAbsent(String key, String value) {
        return  redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    @Override
    public Set<String> keys(String patten) {
        return redisTemplate.keys(patten);
    }

    @Override
    public List<String> mget(Collection<String> list) {
        return  redisTemplate.opsForValue().multiGet(list);
    }
}
