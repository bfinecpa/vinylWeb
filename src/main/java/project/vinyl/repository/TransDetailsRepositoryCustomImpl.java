package project.vinyl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.vinyl.dto.QTransactionDetailsDto;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.*;

import javax.persistence.EntityManager;
import java.util.List;


public class TransDetailsRepositoryCustomImpl implements TransDetailsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public TransDetailsRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TransactionDetailsDto> getTransaction(Long memberId, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QMessageRoom messageRoom = QMessageRoom.messageRoom;
        QTransactionDetails transactionDetails = QTransactionDetails.transactionDetails;

        List<TransactionDetailsDto> val = queryFactory.select(
                        new QTransactionDetailsDto(transactionDetails.id, item.name, itemImg.imgUrl, item.member.name))
                .from(transactionDetails, itemImg)
                .join(transactionDetails.messageRoom, messageRoom)
                .join(itemImg.item, item)
                .where(item.id.eq(messageRoom.item.id))
                .where(transactionDetails.transactionCompleted.get(messageRoom.participateMember1.id)
                        .and(transactionDetails.transactionCompleted.get(messageRoom.participateMember2.id)))
                .where(messageRoom.participateMember1.id.eq(memberId)
                        .or(messageRoom.participateMember2.id.eq(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total =queryFactory
                .select(itemImg.count())
                .from(transactionDetails, itemImg)
                .join(transactionDetails.messageRoom, messageRoom)
                .join(itemImg.item, item)
                .where(item.id.eq(messageRoom.item.id))
                /*.where(transactionDetails.transactionCompleted.get(messageRoom.participateMember1.id)
                        .and(transactionDetails.transactionCompleted.get(messageRoom.participateMember2.id)))
                .where(messageRoom.participateMember1.id.eq(memberId)
                        .or(messageRoom.participateMember2.id.eq(memberId)))*/
                .fetchOne();

        return new PageImpl(val, pageable, total);

    }
}
