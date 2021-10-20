package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {
    @Autowired
    EntityManager em;

    //Rollback 의 default 가 true 여서
    // DB insert 해도 rollback 하기 때문에 데이터가 안보임
    //그래서 value=false 를 줘서 rollback 못하게 막는다
    @Test @Rollback //(value = false)
    public void entity() throws Exception{
        //create Member
        Member member = new Member();
        member.setName("Mongta");
        Address address = new Address("Seoul","DongJak","16169");

        //Address Save at Member
        member.setAddress(address);

        //영속성 컨텍스트에 저장
        em.persist(member);

        //Create Order
        Order order = new Order();
        //Order 와 Member 연결
        order.setMember(member);


        //Create Delivery
        Delivery delivery = new Delivery();
        //              member가 가진 Address를 가져와서 넣어준다
        delivery.setAddress(member.getAddress());

        delivery.setStatus(DeliveryStatus.READY);
        //Order 와 Delivery 연결
        order.setDelivery(delivery);

//        이미 order에 cascade ALL을 줬기 때문에 delivery는 줄 필요 없다
//        em.persist(delivery);


        //Item Book 생성
        Book book = new Book();
        book.setName("Call My Name");
        book.setPrice(25000);
        book.setStockQuantity(30);
        book.setAuthor("Mongta");
        book.setIsbn("298-128c");

       //영속성 컨텍스트에 저장
        em.persist(book);

        //OrderItem - 주문 목록 생성 -> LifeCycle이 Order와 같다 (cascade 되어있음)
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(5);
        orderItem.setOrderPrice(175000);
        orderItem.setItem(book);

        //Order 와 OrderItem 연결 -> order 에 설정해놓은 addOrderItem에 orderItem 추가
        order.addOrderItem(orderItem);

        //주문한 날짜 설정
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        //영속성 컨텍스트에 저장
        em.persist(order);






    }

}