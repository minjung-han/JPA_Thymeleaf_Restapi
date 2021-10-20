package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    //Member 와 다대 일 관계(N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    //foreign key 의 name을 "delivery_id" 로 설정하겠다
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //OrderItem 과 일대 다(1:N)관계
    // cascade : order 를 저장하면서 delivery 도 같이 가져가겠다는 뜻
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //주문 날짜
    private LocalDateTime orderDate;

    //주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //=============연관관계 메서드==============
    //Order 와 Member 관계이기 때문에 order list 를 get 해줘서 add
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    //Order 와 Delivery 관계
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    
    //Order 와 OrderItem 관계
    public void addOrderItem(OrderItem orderItem){
        this.orderItems = orderItems;
        orderItem.setOrder(this);
    }


    //== 비즈니스 로직 : 주문생성 메서드==//
    public static Order createOrder (Member member, Delivery delivery,OrderItem... orderItems) {
        Order order = new Order();
        //주문에 주문한 Member 연결
        order.setMember(member);
        //주문에 배송 연결
        order.setDelivery(delivery);
        
        //주문 한 아이템 갯수만큼 order에 orderItem 추가
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        //order 상태 ORDER 로 변경
        order.setStatus(OrderStatus.ORDER);
        //주문 날짜 현재로
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        //만약 배달 완료된(COMP) 상태라면 상품 취소 불가 exception
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        //배송 전이면 주문 상태 CANCEL 변경
        this.setStatus(OrderStatus.CANCEL);
        //주문 취소한 상품 갯수만큼 재고 수량 증가 처리
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //==비즈니스 로직 : 전체 주문 가격 조회 ==//
    public int getTotalPrice() {
        int totalPrice = 0;
        //주문한 상품 갯수만큼 가격 더하기
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }














}
