package net.runnerdave.resteasy.prac.clientproxy;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientProxyConfig {

    @Value("${users.endpoint.v1.url}")
    private String usersEndpointV1;

    @Bean
    public UserResourceV1 getUserResourceV1() {
        ResteasyClient client = new ResteasyClientBuilderImpl().build();
        ResteasyWebTarget target = client.target(usersEndpointV1);
        UserResourceV1 proxy = target.proxy(UserResourceV1.class);
        return proxy;
    }
}
