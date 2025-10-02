package ltw.bt10.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity @Table(name="products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Min(0)
    private Integer quantity;

    @Column(name="description", length=2000)
    private String desc;

    @DecimalMin(value = "0.0", inclusive = false)
    private Double price;

    // chủ sở hữu sản phẩm
    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    // mỗi product thuộc 1 category
    @ManyToOne @JoinColumn(name="category_id")
    private Category category;
}