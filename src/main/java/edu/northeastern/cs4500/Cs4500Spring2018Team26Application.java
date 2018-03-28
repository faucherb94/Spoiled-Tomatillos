package edu.northeastern.cs4500;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class Cs4500Spring2018Team26Application extends SpringBootServletInitializer {

    @PostConstruct
    void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Cs4500Spring2018Team26Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Cs4500Spring2018Team26Application.class, args);
    }
}
