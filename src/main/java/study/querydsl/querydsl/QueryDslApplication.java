package study.querydsl.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class QueryDslApplication {
    @PersistenceContext
    EntityManager em;
    public static void main(String[] args) {
        SpringApplication.run(QueryDslApplication.class, args);
    }

}
