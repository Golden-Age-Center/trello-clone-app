//package com.sparta.trelloproject.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Slf4j
//@Profile("!test")
//@Configuration
//public class RedisConfig {
//
//    @Value("${spring.data.redis.cluster.nodes}")
//    private List<String> nodes;
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        List<RedisNode> redisNodes = nodes.stream()
//                .map(node -> {
//                    String[] parts = node.split(":");
//                    return new RedisNode(parts[0], Integer.parseInt(parts[1]));
//                }).toList();
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
//        redisClusterConfiguration.setClusterNodes(redisNodes);
//        return new LettuceConnectionFactory(redisClusterConfiguration);
//    }
//
//    @Bean
//    public RedissonClient redissonClient() {
//        final Config config = new Config();
//
//        ClusterServersConfig csc = config.useClusterServers()
//                .setScanInterval(2000)
//                .setConnectTimeout(100)
//                .setTimeout(3000)
//                .setRetryAttempts(3)
//                .setRetryInterval(1500);
//
//        nodes.forEach(node -> csc.addNodeAddress("redis://" + node));
//
//        return Redisson.create(config);
//    }
//}
