package jpastudy.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private  Long id;

    //Order 와 일대 일 관계(1:1)
    @JsonIgnore
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded //: address 의 객체를 참조한다는 의미
    private Address address;

    @Enumerated(EnumType.STRING)
    //EnumType.STRING : DeliveryStatus에 있는 READY와 COMP가
//                      String 문자열로 저장되도록 설정
    private  DeliveryStatus status;
}
