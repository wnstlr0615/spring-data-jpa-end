package study.querydsl.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.querydsl.QHello.hello;

@SpringBootTest
@Transactional
class HelloTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void basicTest(){
        JPAQueryFactory  query=new JPAQueryFactory(em);
        Hello hello = new Hello();
        hello.setName("hello");
        em.persist(hello);

        List<Hello> fetch = query.selectFrom(QHello.hello)
                .fetch();
        for (Hello fetch1 : fetch) {
            System.out.println(hello);
        }
        assertThat(fetch.get(0)).isEqualTo(hello);
    }

}