package ee.kim.veebippod.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {

        // Build a serializer that explicitly injects type metadata into the JSON properties
        GenericJacksonJsonRedisSerializer typedSerializer = GenericJacksonJsonRedisSerializer.builder()
                .typePropertyName("@class") // Explicitly define the JSON property for class metadata
                .enableUnsafeDefaultTyping() // Instructs Jackson to preserve polymorph/class targets
                .build();

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedSerializer));

    }

}
