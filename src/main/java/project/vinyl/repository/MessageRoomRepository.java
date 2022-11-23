package project.vinyl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.vinyl.entity.Member;
import project.vinyl.entity.MessageRoom;

import java.util.List;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    List<MessageRoom> findByParticipateMember1IdOrParticipateMember2Id(Long memberId1, Long memberId2);

    List<MessageRoom> findByItemId(Long itemId);

}
