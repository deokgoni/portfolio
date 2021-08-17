package com.gon.webservice.dto;

import com.gon.webservice.domain.item.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {

    private Long id;

    @NotEmpty(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "상품가격을 입력하세요.")
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull(message = "수량을 입력하세요.")
    @Max(999)
    private Integer stockQuantity;

    @NotEmpty(message = "저자를 입력하세요.")
    private String author;

    @NotEmpty(message = "고유번호를 입력하세요.")
    private String isbn;

    public BookDto(Book entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.stockQuantity = entity.getStockQuantity();
        this.author = entity.getAuthor();
        this.isbn = entity.getIsbn();
    }

}
