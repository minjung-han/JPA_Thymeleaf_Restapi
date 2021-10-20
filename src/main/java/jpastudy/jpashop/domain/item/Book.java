package jpastudy.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
//DiscriminatorValue : 하위 클래스에 선언,
// 엔티티를 저장할 때 슈퍼타입의 구분 컬럼에 저장할 값을 지정
@DiscriminatorValue("Book")
public class Book extends Item{

    private String author;
    private String isbn;


}
