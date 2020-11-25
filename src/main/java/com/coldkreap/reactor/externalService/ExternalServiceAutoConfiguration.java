package com.coldkreap.reactor.externalService;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

/**
 * A class to create Spring Beans.
 *
 * The Configuration annotation tells spring that this class contains the definitions for creating spring beans.
 * A Bean is just an object that gets created and can then be injected into other classes for use.
 * It is a way to separate the construction of an object from its usage. This makes it easy to swap out different
 *   implementations and makes testing very easy as you can test the object in isolation, and test the usage with a mock.
 */
@Configuration
public class ExternalServiceAutoConfiguration {
    /**
     * Creates a WebClient with the given values (that can be loaded from the application.yml).
     *
     * This is now available to Spring for injecting into other classes. We will inject this into our
     * {@link com.coldkreap.reactor.filter.CallExternalServiceGatewayFilterFactory} class.
     */
    @Bean
    public WebClient client(
            // These parameters are injected by Spring and defined in the application.yml.
            @Value("${externalService.connectTimeoutMs}") int connectTimeoutMs,
            @Value("${externalService.readTimeoutMs}") int readTimeoutMs,
            @Value("${externalService.writeTimeoutMs}") int writeTimeoutMs,
            @Value("${externalService.uri}") String uri) {

        // Creating our own TcpClient enables us to set non-default connect, read, and write timeouts.
        final TcpClient tcpClient = TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(writeTimeoutMs, TimeUnit.MILLISECONDS));
            });

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
            .baseUrl(uri)
            .build();
    }
}
