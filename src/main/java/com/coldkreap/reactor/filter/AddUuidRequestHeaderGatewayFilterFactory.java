package com.coldkreap.reactor.filter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * A factory that creates a {@link GatewayFilter} that will add a random UUID as a request header.
 */
@Component
public class AddUuidRequestHeaderGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    /**
     * Constructs the GatewayFilterFactory.
     *
     * We have to tell the parent class what Config class to use for loading route configuration from the application.yml.
     * For this GatewayFilterFactory, we just have a single config that is the name of the header, so we can use the built in NameConfig (from the parent class).
     */
    public AddUuidRequestHeaderGatewayFilterFactory() {
        super(NameConfig.class);
    }

    /**
     * {@inheritDoc}
     *
     * Overriding this method, allows for the shortcut form of configuration.
     * Shortcut form for this filter would look like:
     *   - AddUuidRequestHeader=My-Header-Name
     *
     * Normal form (without this override) would look like:
     *   - name: AddUuidRequestHeader
     *     args:
     *       name: My-Header-Name
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(NAME_KEY);
    }

    @Override
    public GatewayFilter apply(final NameConfig config) {
        // This lambda represents our GatewayFilter implementation
        return (exchange, chain) -> {
            final String headerName = config.getName();

            // Only generate the UUID if the header is missing.
            if (exchange.getRequest().getHeaders().get(headerName) == null) {
                // Since the exchange is all immutable, we need to use the mutate methods of the objects.
                final ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header(headerName, UUID.randomUUID().toString())
                    .build();
                return chain.filter(exchange.mutate().request(request).build());

            } else {
                return chain.filter(exchange);
            }
        };
    }
}
