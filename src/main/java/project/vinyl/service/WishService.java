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


    public Long setWishItem(Long memberId, Long itemId) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        WishList wishList = getWishList(member);
        List<WishItem> byWishListId = wishItemRepository.findByWishListId(wishList.getId());
        for (WishItem wishItem : byWishListId) {
            if (wishItem.getItem().getId()==itemId){
                throw new EntityExistsException();
            }
        }
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);
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
        Page<CRUDWishItemDto> wishItemDto = wishItemRepository.getWishItemDto(wishList.getId(), pageable);

        int number = wishItemDto.getNumber();
        System.out.println("number = " + number);
        return wishItemDto;
    }

    public void deleteWishItem(Long memberId, Long wishItemId){
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
        WishList byMemberId = wishListRepository.findByMemberId(memberId);

        WishItem wishItem = wishItemRepository.findById(wishItemId).orElseThrow(EntityExistsException::new);
        if(wishItem.getWishList()==byMemberId){
            wishItemRepository.delete(wishItem);
        }
    }



}
