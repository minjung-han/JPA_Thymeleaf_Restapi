package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        all.forEach(order -> {
            order.getMember().getName(); //Member LAZY Loading 강제 초기화
            order.getDelivery().getAddress(); //Delivery LAZY Loading 강제 초기화
        });
        return all;
    }

    /* V2 : 엔티티를 DTO로 변환
    * 문제점 : 지연로딩(lazy loading) 으로 쿼리 N번 호출
    * N + 1 문제
    * Order 2건 / Member 2 건 / Delivery 2건
    * order 1 / Member 2 / Delivery 2
    */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream() //Stream<Order>
                .map(order -> new SimpleOrderDto(order)) //Stream<SimpleOrderDto>
                .collect(Collectors.toList()); //List<SimpleOrderDto>
    }


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 강제 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
    }
}

