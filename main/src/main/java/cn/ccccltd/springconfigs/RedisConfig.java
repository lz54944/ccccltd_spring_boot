package cn.ccccltd.springconfigs;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cn.ccccltd.caches.CacheNames;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 配置
 * 
 * @author ccccltd
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	/**
	 * 自定义缓存的可以生成策略, 没有指定key的缓存都使用这个生成.
	 *
	 * @return
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		// 设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
		// 使用：进行分割，可以很多显示出层级关系
		// 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法，我感觉很好用，大家感兴趣可以去了解下
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(".");
			sb.append(method.getName());
			sb.append("[");
			for (Object obj : params) {
				sb.append(String.valueOf(obj));
			}
			sb.append("]");
			return sb.toString();
		};
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		// 设置缓存的默认过期时间，也是使用Duration设置, 设置为30分钟
		config = config.entryTtl(Duration.ofMinutes(30))
				// 不缓存空值
				.disableCachingNullValues();

		// 设置一个初始化的缓存空间set集合, 就是注解@Cacheable(value = "my-redis-cache2")中的value值,
		Set<String> cacheNames = new HashSet<>();
		cacheNames.add(CacheNames.CONFIG);

		Map<String, RedisCacheConfiguration> configMap = new HashMap<>(16);
		// 设置过期时间为 30s
		configMap.put(CacheNames.CONFIG, config.entryTtl(Duration.ofSeconds(30)));

		// 使用自定义的缓存配置初始化一个cacheManager
		return RedisCacheManager.builder(factory)
				// 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
				.initialCacheNames(cacheNames)
				.withInitialCacheConfigurations(configMap)
				.transactionAware()
				.build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		//设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		// 配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		// key序列化
		redisTemplate.setKeySerializer(stringSerializer);
		// value序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// Hash key序列化
		redisTemplate.setHashKeySerializer(stringSerializer);
		// Hash value序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@ConfigurationProperties
	class DataJedisProperties {
		@Value("${spring.redis.host}")
		private String host;
		@Value("${spring.redis.password}")
		private String password;
		@Value("${spring.redis.port}")
		private int port;
		@Value("${spring.redis.timeout}")
		private int timeout;
		@Value("${spring.redis.jedis.pool.max-idle}")
		private int maxIdle;
		@Value("${spring.redis.jedis.pool.max-wait}")
		private long maxWaitMillis;

		@Bean
		JedisConnectionFactory jedisConnectionFactory() {

			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
			redisStandaloneConfiguration.setHostName(host);
			redisStandaloneConfiguration.setPort(port);
			redisStandaloneConfiguration.setDatabase(0);
			redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

			JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
			jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
			return new JedisConnectionFactory(redisStandaloneConfiguration,
					jedisClientConfiguration.build());
		}

		@Bean
		public JedisPool redisPoolFactory() {
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(maxIdle);
			jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

			return new JedisPool(jedisPoolConfig, host, port, timeout, password);
		}

	}

}
