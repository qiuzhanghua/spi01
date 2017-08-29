package com.example.spi01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
public class SpiConfiguration {
  @Bean
  public MessageChannel input() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel toTransform() {
    return new DirectChannel();
  }
  @Bean
  public MessageChannel toLog() {
    return new DirectChannel();
  }


  @MessageEndpoint
  class SimpleFilter {
    @Filter(inputChannel = "input", outputChannel = "toTransform")
    public boolean process(String message) {
      return "World".equals(message);
    }
  }

  @MessageEndpoint
  class SimpleTransformer {
    @Transformer(inputChannel = "toTransform", outputChannel = "toLog")
    public String process(String message) {
      return "Hello ".concat(message);
    }
  }

//  @MessageEndpoint
  class SimpleServiceActivator {
    @ServiceActivator(inputChannel = "toLog")
    public void process(String message) {
      System.out.println(message);
    }
  }

  @Bean
  @ServiceActivator(inputChannel = "toLog")
  public LoggingHandler logging() {
    LoggingHandler adapter = new LoggingHandler(LoggingHandler.Level.INFO);
    adapter.setLoggerName("Simple_Logger");
    adapter.setLogExpressionString("headers.id + ' : ' + payload");
    return adapter;
  }
}
