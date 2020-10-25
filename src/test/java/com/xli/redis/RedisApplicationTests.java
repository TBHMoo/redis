package com.xli.redis;

import com.xli.redis.repository.RedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@SpringBootTest
class RedisApplicationTests {

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private RedisTemplate redisTemplate;
	@Test
	void contextLoads() {
	}

	@Test
	@Rollback(false)
	void redisget(){
		String key = "test";
		String v = redisRepository.get(key);
		System.out.println(v);
	}


	@Test
	@Rollback(false)
	void setIfAbsent(){
		String key = "key5";
		String value=  "value";
		redisRepository.setIfAbsent(key,value);
		Object v = redisRepository.get(key);
		System.out.println(v);
	}

	@Test
	@Rollback(false)
	void keys(){
//		setIfAbsent();

		Set<String>  set = redisTemplate.keys("*");
		for (String temp:set)
		System.out.println(set);
	}


	@Test
	@Rollback(false)
	void get1(){
		String prefix = "keySff";
		for(int i=0;i<10;i++){
			String key = prefix+i;
			String value = "value"+i;
			redisRepository.setIfAbsent(key,value);
		}

		Set<String> set = redisRepository.keys(prefix+"*");
		for(String temp:set){
			System.out.println(redisRepository.get(temp));
		}
		List<String> values = redisRepository.mget(set);
		System.out.println(values.toString());
	}
}
