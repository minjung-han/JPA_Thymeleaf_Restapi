package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * OneToMany 관계 성능 최적화
 * Order -> OrderItem -> Item
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * V1 : 엔티티를 API에 직접 노출하는 방식
     * N + 1 문제 발생함
     * Member / Delivery N번 (Order Row 수 만큼)
     * OrderItem N번 (Order Row 수 만큼)
     * Item N번 (OrderItem Row 수 만큼)
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 강제초기화
        }
        return all;
    }

    /**
     * V2 : 엔티티를 DTO로 변환해서 노출하는 방식
     * N + 1 문제 발생 함
     */

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream() //Stream<Order>
                .map(order -> new OrderDto(order)) //Stream<OrderDto>
                .collect(toList()); //List<OrderDto>

    }

    /**
     * V3 : 엔티티를 DTO 로 변환해서 노출, Fetch Join 사용해서 성능 최적화
     * 문제점 : (One/Many)ToMany 의존 관계의 페이징 처리가 안됨
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        orders.forEach(order -> System.out.println(order));

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * V 3.1 : 엔티티를 DTO로 변환해서 노출 , Fetch Join을 사용해서 성능 최적화
     * ToMany 관계인 엔티티를 가져올 댸 페이징 처리 되지 않는 문제를 해결하기 위해
     * ToOne 관계인 엔티티는 Fetch Join 으로 가져오고
     * ToMany 관계는 hibernate.default_batch_fetch_size 설정하기
     * */

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_Paging(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue= "100") int limit) {
        List<Order> orderList = orderRepository.findAllWithMemberDelivery(offset,limit);
        List<OrderDto> orderDtoList = orderList.stream() //Stream<Order>
                .map(order -> new OrderDto(order)) //Stream<OrderDto>
                .collect(Collectors.toList()); //List<OrderDto>
        return orderDtoList;
    }


    //응답과 요청에 사용할 DTO Inner Class 선언
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                    .stream() // Stream<OrderItem>
                    .map(orderItem -> new OrderItemDto(orderItem)) //Stream(OrderItemDto)
                    .collect(toList()); //List<OrderItemDto>

        }
    } //static class OrderDto


    @Data
    static class OrderItemDto {
        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    } //static class OrderItemDto


}