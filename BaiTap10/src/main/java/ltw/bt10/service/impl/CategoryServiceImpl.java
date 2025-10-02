package ltw.bt10.service.impl;

import ltw.bt10.dto.CategoryDTO;
import ltw.bt10.entity.Category;
import ltw.bt10.repository.CategoryRepository;
import ltw.bt10.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll(pageable);
        }
        return repo.findByNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    @Override
    public Category save(CategoryDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setActive(dto.getActive() != null ? dto.getActive() : true);
        return repo.save(c);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Category update(Long id, CategoryDTO dto) {
        Category c = repo.findById(id).orElseThrow();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setActive(dto.getActive());
        return repo.save(c);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}