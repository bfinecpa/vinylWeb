package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.MessageDto;
import project.vinyl.dto.MessageRoomDto;
import project.vinyl.entity.Item;
import project.vinyl.entity.ItemImg;
import project.vinyl.entity.Member;
import project.vinyl.repository.ItemImgRepository;
import project.vinyl.repository.ItemRepository;
import project.vinyl.repository.MemberRepository;
import project.vinyl.service.MessageRoomService;
import project.vinyl.service.MessageService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {


    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final MessageService messageService;
    private final MessageRoomService messageRoomService;


    @PostConstruct
    public void newMember(){
        Member member = new Member("aa", "aa", "aa", "aa", "123");
        Member member2 = new Member("ss", "ss", "ss", "ss", "123");
        memberRepository.save(member);
        memberRepository.save(member2);
        Item item = new Item("test", "testtest", 10, 10, true, ItemSellStatus.SELL, member);
        itemRepository.save(item);
        ItemImg itemImg = new ItemImg("aa.png", "aa.png", "Y", item, "/image/item/aa.png");
        itemImgRepository.save(itemImg);
    }


    @PostMapping(value= "/message")
    public String startMessage(HttpServletRequest request, @RequestParam Long itemId,
                               @RequestParam Long sellerId){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        messageRoomService.makeRoom(itemId, member.getId(), sellerId);
        return "redirect:/message_list.do";
    }

    @RequestMapping(value = "/message_list.do")
    public String message(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        String nick = member.getName();
        List<MessageRoomDto> msgRoomDto = messageService.getMsgRoomDto(member.getId());
        request.setAttribute("msgRoomDtoList", msgRoomDto);
        return "message/messageService";
    }

    @RequestMapping(value = "/message_ajax_list.do")
    public String message_ajax_list(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        String nick = member.getName();
        List<MessageRoomDto> msgRoomDto = messageService.getMsgRoomDto(member.getId());
        request.setAttribute("msgRoomDtoList", msgRoomDto);
        return "message/message_ajax_list";
    }

    @RequestMapping(value = "/message_content_list.do")
    public String message_content_list(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Long room = Long.parseLong(request.getParameter("room"));
        List<MessageDto> msgDto = messageService.getMsgDto(room);
        /*request.setAttribute("nick" , member.getName());*/
        session.setAttribute("nick", member.getName());
        request.setAttribute("clist", msgDto);
        return "message/message_content_list";
    }

    @ResponseBody
    @RequestMapping(value = "/message_send_inlist.do")
    public void message_send_inlist(@RequestParam Long room, @RequestParam String content, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        // other nick 없애야함
        Long other = messageRoomService.findOther(room, member.getId());
        messageService.sendMessage(member.getId(), other, room, content);
    }
}
