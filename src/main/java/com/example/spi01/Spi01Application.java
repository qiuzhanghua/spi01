package com.example.spi01;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

//@ImportResource("spi-context.xml")
@EnableIntegration
@SpringBootApplication
public class Spi01Application {

//  @Bean
  public IntegrationFlow simpleFlow() {
    return IntegrationFlows.from(MessageChannels.direct("input"))
        .filter("World"::equals)
        .transform("Hello "::concat)
        .handle(System.out::println)
        .get();
  }

  @Bean
  CommandLineRunner process(MessageChannel input) {
    return args -> {
      input.send(MessageBuilder.withPayload("World").build());
    };
  }

	public static void main(String[] args) {
		SpringApplication.run(Spi01Application.class, args);
	}

}
