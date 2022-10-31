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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public boolean existRoom(Long roomId){
        Optional<MessageRoom> messageRoom = messageRoomRepository.findById(roomId);
        return (messageRoom.isEmpty()) ? false : true;
    }

    public void makeRoom(Long itemId, Long member1Id, Long member2Id){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        Member member1 = memberRepository.findById(member1Id).orElseThrow(EntityExistsException::new);
        Member member2 = memberRepository.findById(member2Id).orElseThrow(EntityExistsException::new);
        MessageRoom messageRoom= new MessageRoom(item, member1, member2);
        messageRoomRepository.save(messageRoom);
    }

    public Long findOther(Long roomId, Long memberId){
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(EntityExistsException::new);
        return (messageRoom.getParticipateMember1().getId()==memberId) ? messageRoom.getParticipateMember2().getId()
                : messageRoom.getParticipateMember1().getId();
    }

}
