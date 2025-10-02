package ltw.bt10.service;

import ltw.bt10.dto.CategoryDTO;
import ltw.bt10.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<Category> search(String keyword, Pageable pageable);
    Category save(CategoryDTO dto);
    Optional<Category> findById(Long id);
    Category update(Long id, CategoryDTO dto);
    void deleteById(Long id);
}