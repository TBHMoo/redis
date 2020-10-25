package com.xli.redis.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface RedisRepository {



    String get(String key);

    Boolean setIfAbsent(String key ,String value);

    Set<String> keys(String patter);

    List<String> mget(Collection<String> list);


}