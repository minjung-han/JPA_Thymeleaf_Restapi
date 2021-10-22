package jpastudy.jpashop.repository.order.query;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
// 값이 같고 HashCode 가 같은지를 물어보는데 orderId를 줘서 orderId 만 같아도 동일하게 취급하라는 것
//API 에서 응답으로 나갈 때 주는 Dto 가 아니라 query 를 수행한 결과를 담아주는 Dto
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}