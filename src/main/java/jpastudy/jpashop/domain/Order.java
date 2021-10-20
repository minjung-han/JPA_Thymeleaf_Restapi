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


}
