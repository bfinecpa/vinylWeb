package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.lang.module.FindException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final WishListRepository wishListRepository;
    private final WishItemRepository wishItemRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public WishList getWishList(Member member){
        WishList wishList = wishListRepository.findByMemberId(member.getId());
        if(wishList==null){
            wishList=new WishList(member);
            wishListRepository.save(wishList);
        }
        return wishList;
    }


    public Long setWishItem(Long memberId, Long itemId) throws Exception {
        WishList wishList = getWishList(memberService.findById(memberId));
        for (WishItem wishItem : wishItemRepository.findByWishListId(wishList.getId())) {
            if (wishItem.getItem().getId()==itemId){
                throw new EntityExistsException();
            }
        }
        Item item = itemService.findItemByItemId(itemId);
        if (item.getMember().getId()==memberId){
            throw new FindException();
        }
        WishItem wishItem = new WishItem(item, wishList);
        wishItemRepository.save(wishItem);
        return wishItem.getId();
    }


    public Page<CRUDWishItemDto> getWishItem(Long memberId, Pageable pageable){
        WishList wishList = wishListRepository.findByMemberId(memberId);
        if(wishList==null){
            return null;
        }
        return wishItemRepository.getWishItemDto(wishList.getId(), pageable);
    }

    public void deleteWishItem(Long memberId, Long wishItemId){

        WishItem wishItem = wishItemRepository.findById(wishItemId).orElseThrow(EntityExistsException::new);

        if(wishItem.getWishList()==wishListRepository.findByMemberId(memberId)){
            wishItemRepository.delete(wishItem);
        }
    }

}
