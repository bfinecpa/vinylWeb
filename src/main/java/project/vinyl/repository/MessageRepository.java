package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.vinyl.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMessageRoomId(Long msgRoomId);
}
