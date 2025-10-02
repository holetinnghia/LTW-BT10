package ltw.bt10.controller;

import jakarta.validation.Valid;
import ltw.bt10.dto.CategoryDTO;
import ltw.bt10.entity.Category;
import ltw.bt10.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "5") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> data = service.search(q, pageable);
        model.addAttribute("data", data);
        model.addAttribute("q", q);
        return "category/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "category/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("category") CategoryDTO dto, BindingResult br, Model model) {
        if (br.hasErrors()) return "category/form";
        service.save(dto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category c = service.findById(id).orElseThrow();
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setActive(c.getActive());
        model.addAttribute("category", dto);
        return "category/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("category") CategoryDTO dto, BindingResult br) {
        if (br.hasErrors()) return "category/form";
        service.update(id, dto);
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/admin/categories";
    }
}