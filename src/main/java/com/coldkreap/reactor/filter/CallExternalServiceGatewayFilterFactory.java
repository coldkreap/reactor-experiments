package com.coldkreap.reactor.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CallExternalServiceGatewayFilterFactory {

    private final WebClient client;

    /**
     * The client gets injected by Spring. The client is the one constructed in the
     * {@link com.coldkreap.reactor.externalService.ExternalServiceAutoConfiguration} class.
     *
     * @param client The client to use for making the external REST call.
     */
    public CallExternalServiceGatewayFilterFactory(final WebClient client) {

        this.client = client;
    }
}
