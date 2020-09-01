package net.runnerdave.resteasy.prac.config;

import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@Configuration
@ApplicationPath("/app")
public class ReasteasyConfig extends Application {
}
