package ltw.bt10.graphql;

import ltw.bt10.model.*;
import ltw.bt10.repository.*;
import ltw.bt10.service.UserService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Controller
public class ProductUserCategoryGraphQL {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    public ProductUserCategoryGraphQL(ProductRepository p, CategoryRepository c, UserRepository u, UserService userService) {
        this.productRepo = p; this.categoryRepo = c; this.userRepo = u; this.userService = userService;
    }

    // Queries
    @QueryMapping
    public List<Product> productsSortedByPrice() { return productRepo.findAllByOrderByPriceAsc(); }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) { return productRepo.findByCategoryId(categoryId); }

    @QueryMapping
    public List<User> users() { return userRepo.findAll(); }
    @QueryMapping
    public Optional<User> user(@Argument Long id) { return userRepo.findById(id); }

    @QueryMapping
    public List<Category> categories() { return categoryRepo.findAll(); }
    @QueryMapping
    public Optional<Category> category(@Argument Long id) { return categoryRepo.findById(id); }

    @QueryMapping
    public List<Product> products() { return productRepo.findAll(); }
    @QueryMapping
    public Optional<Product> product(@Argument Long id) { return productRepo.findById(id); }

    // Field mapping: map roles to string
    @SchemaMapping(typeName = "User", field = "roles")
    public List<String> roles(User u) {
        return u.getRoles().stream().map(Role::getName).toList();
    }

    // Mutations
    @MutationMapping @Transactional
    public User createUser(@Argument("input") UserInput input) {
        var u = User.builder()
                .fullname(input.fullname())
                .email(input.email())
                .password(input.password())
                .phone(input.phone())
                .build();
        return userService.create(u);
    }

    @MutationMapping @Transactional
    public User updateUser(@Argument Long id, @Argument("input") UserInput input) {
        var u = userRepo.findById(id).orElseThrow();
        u.setFullname(input.fullname());
        u.setEmail(input.email());
        if (input.password()!=null && !input.password().isBlank()) u.setPassword(input.password());
        u.setPhone(input.phone());
        return userRepo.save(u);
    }

    @MutationMapping @Transactional
    public Boolean deleteUser(@Argument Long id) { userRepo.deleteById(id); return true; }

    @MutationMapping @Transactional
    public Category createCategory(@Argument("input") CategoryInput in) {
        return categoryRepo.save(Category.builder().name(in.name()).images(in.images()).build());
    }

    @MutationMapping @Transactional
    public Category updateCategory(@Argument Long id, @Argument("input") CategoryInput in) {
        var c = categoryRepo.findById(id).orElseThrow();
        c.setName(in.name()); c.setImages(in.images());
        return categoryRepo.save(c);
    }

    @MutationMapping @Transactional
    public Boolean deleteCategory(@Argument Long id) { categoryRepo.deleteById(id); return true; }

    @MutationMapping @Transactional
    public Product createProduct(@Argument("input") ProductInput in) {
        var p = Product.builder()
                .title(in.title()).quantity(in.quantity()).desc(in.desc()).price(in.price())
                .build();
        if (in.userId()!=null) p.setUser(userRepo.findById(in.userId()).orElse(null));
        if (in.categoryId()!=null) p.setCategory(categoryRepo.findById(in.categoryId()).orElse(null));
        return productRepo.save(p);
    }

    @MutationMapping @Transactional
    public Product updateProduct(@Argument Long id, @Argument("input") ProductInput in) {
        var p = productRepo.findById(id).orElseThrow();
        if (in.title()!=null) p.setTitle(in.title());
        if (in.quantity()!=null) p.setQuantity(in.quantity());
        if (in.desc()!=null) p.setDesc(in.desc());
        if (in.price()!=null) p.setPrice(in.price());
        if (in.userId()!=null) p.setUser(userRepo.findById(in.userId()).orElse(null));
        if (in.categoryId()!=null) p.setCategory(categoryRepo.findById(in.categoryId()).orElse(null));
        return productRepo.save(p);
    }

    @MutationMapping @Transactional
    public Boolean deleteProduct(@Argument Long id) { productRepo.deleteById(id); return true; }

    // record inputs
    public record UserInput(String fullname, String email, String password, String phone) {}
    public record CategoryInput(String name, String images) {}
    public record ProductInput(String title, Integer quantity, String desc, Double price, Long userId, Long categoryId) {}
}