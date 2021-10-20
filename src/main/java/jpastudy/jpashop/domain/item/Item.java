package jpastudy.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
//Inheritance : DB 에서 논리모델을 물리모델로 구현하는 방법, 부모 클래스에 사용
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    //상품 이름
    private String name;

    //상품 가격
    private int price;

    //재고 수량
    private int stockQuantity;



}
