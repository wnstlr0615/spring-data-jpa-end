package study.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring.data.jpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
