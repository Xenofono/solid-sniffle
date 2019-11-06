package com.sti.project.projekt.blog;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfig extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("ec2-54-235-114-242.compute-1.amazonaws.com")
                        .port(5432)
                        .username("uayhdvkogsfifx")
                        .password("4c03761bbd0d9489fef07279998a8880d3728cdc03470d94a09e95ffdf89a66c")
                        .database("d4nkkkou00kpb2")
                        .build());
    }
}
