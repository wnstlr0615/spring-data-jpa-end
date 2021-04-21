package study.spring.data.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring.data.jpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TeamRepositoryTest {
    @Autowired
    TeamRepository teamRepository;
    @Test
    @Rollback(false)
    public void basicCRUD(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        //조회 검증
        Team findTeamA = teamRepository.findById(teamA.getId()).get();
        Team findTeamB = teamRepository.findById(teamB.getId()).get();
        assertThat(teamA.getId()).isEqualTo(findTeamA.getId());
        assertThat(teamB.getId()).isEqualTo(findTeamB.getId());
        assertThat(teamA).isEqualTo(findTeamA);
        assertThat(teamB).isEqualTo(findTeamB);

        //리스트 검증
        List<Team> teams = teamRepository.findAll();
        assertThat(teams.size()).isEqualTo(2);

        //카운트 검증
        long count = teamRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
         teamRepository.delete(teamA);
        long deleteCnt = teamRepository.count();
        assertThat(deleteCnt).isEqualTo(1);



    }
}