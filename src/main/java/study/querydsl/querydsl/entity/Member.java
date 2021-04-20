package study.querydsl.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NamedQuery(name="Member.findByUsername",
            query = "select m from Member m where m.username =:username"
)
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"team"})
public class Member extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username=username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age=age;
        if(team!=null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age=age;
        team=null;
    }

    public void changeTeam(Team team){
        if(this.team!=null){
            team.members.remove(this);
        }
        this.team=team;
        team.getMembers().add(this);
    }
}
