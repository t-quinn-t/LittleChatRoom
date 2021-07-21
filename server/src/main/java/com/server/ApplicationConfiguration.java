package com.server;

import org.hibernate.boot.model.source.spi.JdbcDataType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableJpaRepositories("com.server.repo")
public class ApplicationConfiguration {

    // Set up DB Connection
    @Bean
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/LittleChatRoom");
        dataSource.setUsername("admin");
        dataSource.setPassword("333");
        return dataSource;
    }

}
