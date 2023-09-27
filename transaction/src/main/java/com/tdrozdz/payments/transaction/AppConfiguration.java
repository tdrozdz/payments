package com.tdrozdz.payments.transaction;


import com.tdrozdz.payments.api.VerificationGrpc;
import com.tdrozdz.payments.api.VerificationGrpc.VerificationFutureStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import javax.sql.DataSource;

@Configuration
@EnableRetry
public class AppConfiguration {

  private final Channel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
      .usePlaintext()
      .build();

  @Bean
  VerificationFutureStub createVerificationFutureStub() {
    return VerificationGrpc.newFutureStub(channel);
  }

  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }
}
