package ca.bc.gov.open.jag.aireviewerapi.cache;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Cache AutoConfiguration Test Suite")
public class AutoConfigurationTest {

    private static final String TEST_CRED = "notapassword";
    private static final String HOST = "notip";

    @Mock
    private RedisProperties redisProperties;

    private CacheConfig sut;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private CacheProperties.Redis redisPropertiesMock;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(redisPropertiesMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
        Mockito.when(cachePropertiesMock.getRedis()).thenReturn(redisPropertiesMock);

        sut = new CacheConfig();
        redisProperties = Mockito.mock(RedisProperties.class);

    }

    @DisplayName("stand alone input should generate jedisConnectionFactory")
    @Test
    public void standaloneInputShouldGenerateJedisConnectionFactory() {
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getSentinel()).thenReturn(null);
        Mockito.when(redisProperties.getHost()).thenReturn(HOST);
        Mockito.when(redisProperties.getPort()).thenReturn(6379);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("cluster input should generate jedisConnectionFactory")
    @Test
    public void clusterInputshouldGenerateJedisConnectionFactory() {
        RedisProperties.Cluster cluster = Mockito.mock(RedisProperties.Cluster.class);
        Mockito.when(redisProperties.getCluster()).thenReturn(cluster);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName("sentinel input should generate jedisConnectionFactory")
    @Test
    public void sentinelInputShouldGenerateJedisConnectionFactory() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        JedisConnectionFactory jedisConnectionFactory = sut.jedisConnectionFactory(redisProperties);
        Assertions.assertNotNull(jedisConnectionFactory);
    }

    @DisplayName(" sentinel input should generate exception")
    @Test
    public void sentinelInputShouldThrowIllegalState() {
        RedisProperties.Sentinel sentinel = Mockito.mock(RedisProperties.Sentinel.class);
        Mockito.when(sentinel.getMaster()).thenReturn("master");
        Mockito.when(sentinel.getNodes()).thenReturn(Arrays.asList("TEST:TEST","TEST:TEST"));
        Mockito.when(redisProperties.getSentinel()).thenReturn(sentinel);
        Mockito.when(redisProperties.getCluster()).thenReturn(null);
        Mockito.when(redisProperties.getPassword()).thenReturn(TEST_CRED);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            sut.jedisConnectionFactory(redisProperties);
        });
    }



}
