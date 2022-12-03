package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.vinyl.entity.Item;
import project.vinyl.entity.Member;
import project.vinyl.entity.MessageRoom;
import project.vinyl.repository.ItemRepository;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.MessageRoomRepository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;
    private final ItemService itemService;
    private final MemberService memberService;

    public boolean existRoom(Long roomId){
        Optional<MessageRoom> messageRoom = messageRoomRepository.findById(roomId);
        return (messageRoom.isEmpty()) ? false : true;
    }

    public void makeRoom(Long itemId, Long member1Id, Long member2Id){
        MessageRoom messageRoom= new MessageRoom(itemService.findItemByItemId(itemId),
                memberService.findById(member1Id),  memberService.findById(member2Id));
        messageRoomRepository.save(messageRoom);
    }

    public Long findOther(Long roomId, Long memberId){
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(EntityExistsException::new);
        return (messageRoom.getParticipateMember1().getId()==memberId) ? messageRoom.getParticipateMember2().getId()
                : messageRoom.getParticipateMember1().getId();
    }

    public Boolean existMember(Long memberId, Long roomId){
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(EntityExistsException::new);
        return (messageRoom.getParticipateMember1().getId()==memberId &&
                messageRoom.getParticipateMember2().getId()==memberId) ? true : false ;
    }

    public List<MessageRoom> findByParticipateMembers(Long memberId1, Long memberId2){
        return messageRoomRepository.findByParticipateMember1IdOrParticipateMember2Id(memberId1, memberId2);
    }

    public void save(MessageRoom messageRoom){
        messageRoomRepository.save(messageRoom);
    }

    public MessageRoom findMessageRoomById(Long roomId){
        return messageRoomRepository.findById(roomId).orElseThrow(EntityExistsException::new);
    }


}
