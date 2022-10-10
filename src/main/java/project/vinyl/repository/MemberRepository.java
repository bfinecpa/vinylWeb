package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.vinyl.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
