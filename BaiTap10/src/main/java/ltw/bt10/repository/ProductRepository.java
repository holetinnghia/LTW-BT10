package ltw.bt10.repository;
import ltw.bt10.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findByCategoryId(Long categoryId);
}