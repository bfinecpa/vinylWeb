package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.vinyl.dto.CRUDWishItemDto;
import project.vinyl.entity.Item;
import project.vinyl.entity.Member;
import project.vinyl.entity.WishItem;
import project.vinyl.entity.WishList;
import project.vinyl.repository.ItemRepository;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.WishItemRepository;
import project.vinyl.repository.WishListRepository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishService {

    private final MemberRepository memberRepository;

    private final WishListRepository wishListRepository;
    private final WishItemRepository wishItemRepository;
    private final ItemRepository itemRepository;

    public WishList getWishList(Member member){
        WishList wishList = wishListRepository.findByMemberId(member.getId());
        if(wishList==null){
            wishList=new WishList(member);
            wishListRepository.save(wishList);
        }
        return wishList;
    }

    public Long setWishItem(Long memberId, Long itemId){
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        WishList wishList = getWishList(member);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
        WishItem wishItem = new WishItem(item, wishList);
        wishItemRepository.save(wishItem);
        return wishItem.getId();
    }


    public Page<CRUDWishItemDto> getWishItem(Long memberId, Pageable pageable){
        WishList wishList = wishListRepository.findByMemberId(memberId);
        return wishItemRepository.getWishItemDto(wishList.getId(), pageable);
    }



}
