package ltw.bt10.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min=3, max=60)
    private String fullname;

    @Email @NotBlank @Column(unique = true)
    private String email;

    @NotBlank @Size(min=6)
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message="Số điện thoại không hợp lệ")
    private String phone;

    @ManyToMany
    @JoinTable(name = "user_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}