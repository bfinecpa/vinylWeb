package project.vinyl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.vinyl.dto.CRUDWishItemDto;
import project.vinyl.dto.QCRUDWishItemDto;
import project.vinyl.entity.QItem;
import project.vinyl.entity.QItemImg;
import project.vinyl.entity.QWishItem;

import javax.persistence.EntityManager;
import java.util.List;

public class WishItemRepositoryCustomImpl implements WishItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public WishItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CRUDWishItemDto> getWishItemDto(Long wishListId, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QWishItem wishItem = QWishItem.wishItem;

        List<CRUDWishItemDto> fetch = queryFactory
                .select(new QCRUDWishItemDto(item.id, item.name, item.price, item.negotiation, itemImg.imgUrl, item.itemSellStatus))
                .from(wishItem,itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(wishItem.wishList.id.eq(wishListId))
                .orderBy(wishItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(wishItem.count())
                .from(wishItem, itemImg )
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(wishItem.wishList.id.eq(wishListId))
                .fetchOne();

        return new PageImpl(fetch, pageable, total);
    }
}
