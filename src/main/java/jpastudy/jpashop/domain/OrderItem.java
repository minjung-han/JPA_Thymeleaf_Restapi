package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    //Order 와의 다대 일 관계(N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //item 과 다대 일 관계(N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //주문 가격
    private int orderPrice;

    //주문 수량
    private int count;

    //주문 Item 생성 메소드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        //OrderItem(주문 상품)과 Item(상품) 연결
        orderItem.setItem(item);
        //주문한 상품의 가격
        orderItem.setOrderPrice(orderPrice);
        //주문한 상품의 수량
        orderItem.setCount(count);
        //상품 재고 수량 감소
        item.removeStock(count);

        return orderItem;

    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        //주문이 취소되면 재고 수량이 증가
        getItem().addStock(this.count);
    }
    //==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
    public int getTotalPrice() {
            //    주문 상품 가격 * 수량
        return getOrderPrice() * getCount();
    }
















}
