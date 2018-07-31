package com.kazan.application;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.kazan" })
@EnableJpaRepositories(basePackages = {"com.kazan"})
@EntityScan(basePackages = {"com.kazan"})
@EnableTransactionManagement
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		Environment env = app.run(args).getEnvironment();
		LOGGER.info("\n----------------------------------------------------------\n\t" +
				"Local: \t\thttp://localhost:{}\n\t",
				env.getProperty("server.port"));
    }
	
	@Bean  
	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf){  
	    return hemf.getSessionFactory();  
	}
}
