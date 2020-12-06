package com.coldkreap.reactor.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CallExternalServiceGatewayFilterFactory
        extends AbstractGatewayFilterFactory<CallExternalServiceGatewayFilterFactory.Config> {

    private static final Logger LOG = LoggerFactory.getLogger(CallExternalServiceGatewayFilterFactory.class);

    private final WebClient client;

    /**
     * The client gets injected by Spring. The client is the one constructed in the
     * {@link com.coldkreap.reactor.externalService.ExternalServiceAutoConfiguration} class.
     *
     * @param client The client to use for making the external REST call.
     */
    public CallExternalServiceGatewayFilterFactory(final WebClient client) {
        super(Config.class);
        this.client = client;
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return ((exchange, chain) -> {

            final Mono<String> stringMono = client
                .get() // TODO: Set up stubs to call and actually get data back with.
                .uri("/health")
                .exchange()
                .flatMap(this::processResponse);

            return HystrixCommands.from(stringMono).commandName("externalService").toMono()
                .flatMap(responseString -> {
                    // TODO: do something with response string
                    LOG.info("Deserialized Body => {}.", responseString);
                    return chain.filter(exchange);
                });
        });
    }

    private Mono<String> processResponse(final ClientResponse clientResponse) {
        LOG.info("Processing Response.");
        return clientResponse.bodyToMono(String.class);
    }

    public static class Config {
        // TODO: Demonstrate custom configuration. If not here, then simplify this and I can make a GWFF just for demonstrating custom config.
    }
}
