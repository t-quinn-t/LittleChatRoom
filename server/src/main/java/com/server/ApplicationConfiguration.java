package com.server;

import com.server.config.WebSocketConfiguration;
import com.server.model.User;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@Import({WebSocketConfiguration.class})
public class ApplicationConfiguration {

    /*  ----- ----- ----- Utility Bean Setup ----- ----- ----- */
    @Bean
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/LittleChatRoom");
        dataSource.setUsername("admin");
        dataSource.setPassword("333");
        return dataSource;
    }

    @Bean
    public BCryptPasswordEncoder registerPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*  ----- ----- ----- Model Beans Setup ----- ----- ----- */
    @Bean
    @Scope("prototype")
    public User makeUser() {
        return new User();
    }
}
