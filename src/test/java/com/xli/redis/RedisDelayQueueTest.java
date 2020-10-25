package com.xli.redis;


import com.xli.redis.delayqueue.RedisDelayQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringBootTest
public class RedisDelayQueueTest {

        @Autowired
        private RedisTemplate redisTemplate;

        RedisDelayQueue<String> queue =null;

        final static String delayQueueKey = "xli-delay-queue";

        @PostConstruct
        public void init(){
            queue = new RedisDelayQueue<>(redisTemplate,"xli-delay-queue");
        }

        @Test
        void test(){
            Thread producer = new Thread(){
                @Override
                public void run() {
                    for(int i=0;i<10 ;i++){
                        queue.delay("msg"+i,60);
                    }
                }
            };

            Thread consumer = new Thread(){
                @Override
                public void run() {
                    queue.loop();
                }
            };
            producer.start();
            consumer.start();
            try {
                producer.join();
                Thread.sleep(6000);
//                consumer.interrupt();
//                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Test
        void testrangebyscore(){
            Set<String> set = redisTemplate.opsForZSet().rangeByScore(delayQueueKey, 0, System.currentTimeMillis(), 0, 2);
            for (String temp:set)
            System.out.println(temp);
//            redisTemplate.opsForZSet().remove(delayQueueKey,set.iterator().next());
        }


}
