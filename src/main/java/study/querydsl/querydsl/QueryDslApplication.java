package study.querydsl.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class QueryDslApplication {
    @PersistenceContext
    EntityManager em;
    public static void main(String[] args) {
        SpringApplication.run(QueryDslApplication.class, args);
    }
    @Bean
    public AuditorAware<String> auditorProvider(){
        return ()-> Optional.of(UUID.randomUUID().toString());
    }
}
