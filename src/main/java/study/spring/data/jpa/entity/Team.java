package study.spring.data.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(exclude = {"members"})
public class Team  extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    public Team(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "team")
    List<Member> members=new ArrayList<>();

}
