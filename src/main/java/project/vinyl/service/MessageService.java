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

    private final MessageRepository messageRepository;
    private final MessageRoomService messageRoomService;
    private final MemberService memberService;
    private final ItemService itemService;

    //메세지 목록에 메세지 룸 리스트를 가져올것
    //메세지 룸 목록 dto로 변환
    public List<MessageRoomDto> getMsgRoomDto(Long memberId){
        List<MessageRoom> msgRoomList = messageRoomService.findByParticipateMembers(memberId, memberId);
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
            messageDtoList.add(MessageDto.builder()
                    .id(message.getId())
                    .roomId(message.getMessageRoom().getId())
                    .content(message.getContent())
                    .sendMemberId(message.getSendMember().getId())
                    .receiveMemberId(message.getReceiveMember().getId())
                    .sendMemberName(message.getSendMember().getName())
                    .receiveMemberName(message.getReceiveMember().getName())
                    .build());
        }
        return messageDtoList;
    }

    //메세지 보내기
    //두가지로 나눌 수 있음 -> 처음 보내는 거냐 아니면 있던 룸에서 보낸는 거냐 -> 룸을 만드는 서비스를 생성하자
    public void firstSendMessage(Long sendMemberId, Long receiveMemberId, Long itemId, String content){
        Item item = itemService.findItemByItemId(itemId);
        Member member1 = memberService.findById(sendMemberId);
        Member member2 = memberService.findById(receiveMemberId);
        MessageRoom messageRoom = MessageRoom.builder()
                .item(item)
                .participateMember1(member1)
                .participateMember2(member2)
                .build();
        messageRoomService.save(messageRoom);
        messageRepository.save(Message.builder()
                .sendMember(member1)
                .receiveMember(member2)
                .messageRoom(messageRoom)
                .content(content)
                .build());
    }

    public void sendMessage(Long sendMemberId, Long receiveMemberId, Long roomId, String content){
        messageRepository.save(Message.builder()
                .sendMember(memberService.findById(sendMemberId))
                .receiveMember(memberService.findById(receiveMemberId))
                .messageRoom(messageRoomService.findMessageRoomById(roomId))
                .content(content)
                .build());
    }

}
