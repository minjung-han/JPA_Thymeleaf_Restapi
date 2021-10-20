package jpastudy.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BookForm {
    private Long id;
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;
    private int price;
    @Min(value = 1,message = "재고의 최소 갯수가 1개 이상이어야합니다.")
    private int stockQuantity;
    private String author;
    private String isbn;
}