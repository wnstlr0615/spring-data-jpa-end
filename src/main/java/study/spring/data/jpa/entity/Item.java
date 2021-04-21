package study.spring.data.jpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity implements Persistable<String> {
    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreateDate()==null;
    }
}
