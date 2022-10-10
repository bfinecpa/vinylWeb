package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.vinyl.entity.Member;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
}
