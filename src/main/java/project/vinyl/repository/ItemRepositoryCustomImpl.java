package project.vinyl.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import project.vinyl.dto.ItemSearchDto;
import project.vinyl.dto.MainPageItemDto;
import project.vinyl.dto.QMainPageItemDto;
import project.vinyl.entity.QItem;
import project.vinyl.entity.QItemImg;

import javax.persistence.EntityManager;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MainPageItemDto> search(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainPageItemDto> fetch = queryFactory
                .select(new QMainPageItemDto(item.id, item.name, item.price, itemImg.imgUrl, item.itemSellStatus))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total =queryFactory
                .select(itemImg.count()).from(itemImg)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl(fetch, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.equals(searchQuery, "") ? null:QItem.item.name.like("%"+searchQuery+"%");
    }
}



