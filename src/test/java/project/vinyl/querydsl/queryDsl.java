package project.vinyl.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;
import project.vinyl.entity.QItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class queryDsl {

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before(){
        for(int i=0; i<5; i++){
            Item item = new Item();
            item.setName("a"+i);
            item.setDetails("aaaaa"+i);
            item.setPrice(1000+i);
            item.setStockNumber(10+i);
            item.setNegotiation(true);
            item.setItemSellStatus(ItemSellStatus.SELL);

            em.persist(item);
        }
        for(int i=0; i<5; i++){
            Item item = new Item();
            item.setName("b"+i);
            item.setDetails("bbbbb"+i);
            item.setPrice(500+i);
            item.setStockNumber(50+i);
            item.setNegotiation(false);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);

            em.persist(item);
        }
    }


    @Test
    void search(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = new QItem("item");

        List<Item> items = queryFactory
                .selectFrom(qItem)
                .orderBy(qItem.name.desc())
                .fetch();


        Item item = items.get(0);
        log.info(item.getDetails());
    }

    @Test
    void count111(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = new QItem("item");

        Long aLong = queryFactory
                .select(qItem.count())
                .from(qItem)
                .fetchOne();

        log.info(String.valueOf(aLong));
    }

}
