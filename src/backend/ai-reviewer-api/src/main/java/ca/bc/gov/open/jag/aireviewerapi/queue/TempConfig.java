package ca.bc.gov.open.jag.aireviewerapi.queue;

<<<<<<< HEAD:src/backend/ai-reviewer-api/src/main/java/ca/bc/gov/open/jag/efilingreviewerapi/queue/TempConfig.java
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
=======
import ca.bc.gov.open.jag.aidiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.aireviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.aireviewerapi.document.DocumentsApiDelegateImpl;
>>>>>>> 377a1fa68ff1a40e5a3bd176647681cb18080cee:src/backend/ai-reviewer-api/src/main/java/ca/bc/gov/open/jag/aireviewerapi/queue/TempConfig.java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableCaching
public class TempConfig {

    @Bean
    RedisMessageListenerContainer container(JedisConnectionFactory jedisConnectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("documentWait"));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        return new StringRedisTemplate(jedisConnectionFactory);
    }

    @Bean
    public Receiver receiver(DocumentsApiDelegate documentsApiDelegate, DiligenService diligenService, StringRedisTemplate stringRedisTemplate) {
        return new Receiver(2, documentsApiDelegate, diligenService, stringRedisTemplate);
    }
}
