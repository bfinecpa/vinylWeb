package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.dto.MessageDto;
import project.vinyl.dto.MessageRoomDto;
import project.vinyl.entity.Item;
import project.vinyl.entity.Member;
import project.vinyl.entity.Message;
import project.vinyl.entity.MessageRoom;
import project.vinyl.repository.*;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    //메세지 목록에 메세지 룸 리스트를 가져올것
    //메세지 룸 목록 dto로 변환
    public List<MessageRoomDto> getMsgRoomDto(Long memberId){
        List<MessageRoom> msgRoomList = messageRoomRepository.findByParticipateMember1IdOrParticipateMember2Id(memberId, memberId);
        List<MessageRoomDto> msgRoomDtoList = new ArrayList<>();
        for (MessageRoom messageRoom : msgRoomList) {
            msgRoomDtoList.add(new MessageRoomDto(messageRoom.getId(), messageRoom.getItem().getName()));
        }
        return msgRoomDtoList;
    }

    //방을 누르면 방에 있는 내용 가져오기
    public List<MessageDto> getMsgDto(Long msgRoomId){
        List<Message> msgList = messageRepository.findByMessageRoomId(msgRoomId);
        List<MessageDto> messageDtoList = new ArrayList<>();
        for (Message message : msgList) {
            messageDtoList.add(new MessageDto(message.getId(),message.getMessageRoom().getId(), message.getContent(), message.getSendMember().getId(),
                    message.getReceiveMember().getId(), message.getSendMember().getName(), message.getReceiveMember().getName()));
        }
        return messageDtoList;
    }

    //메세지 보내기
    //두가지로 나눌 수 있음 -> 처음 보내는 거냐 아니면 있던 룸에서 보낸는 거냐 -> 룸을 만드는 서비스를 생성하자
    public void firstSendMessage(Long sendMemberId, Long receiveMemberId, Long itemId, String content){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        Member member1 = memberRepository.findById(sendMemberId).orElseThrow(EntityExistsException::new);
        Member member2 = memberRepository.findById(receiveMemberId).orElseThrow(EntityExistsException::new);
        MessageRoom messageRoom = new MessageRoom(item, member1, member2);
        messageRoomRepository.save(messageRoom);
        Message message = new Message(member1, member2, messageRoom, content);
        messageRepository.save(message);
    }

    public void sendMessage(Long sendMemberId, Long receiveMemberId, Long roomId, String content){
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(EntityExistsException::new);
        Member sendMember = memberRepository.findById(sendMemberId).orElseThrow(EntityExistsException::new);
        Member receiveMember = memberRepository.findById(receiveMemberId).orElseThrow(EntityExistsException::new);
        Message message = new Message(sendMember, receiveMember, messageRoom, content);
        messageRepository.save(message);
    }

}
