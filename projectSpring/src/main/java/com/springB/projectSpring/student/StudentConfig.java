package com.springB.projectSpring.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository repository) {
        return args -> {
            Student lamar = new Student(
                    "Lamar",
                    "lamar@gmail.com",
                    LocalDate.of(2000, Month.JANUARY, 5)
            );
            Student anton = new Student(
                    "Anton",
                    "anton@gmail.com",
                    LocalDate.of(1997, Month.FEBRUARY, 14)
            );
            repository.saveAll(
                    List.of(lamar, anton)
            );
        };
    }
}
