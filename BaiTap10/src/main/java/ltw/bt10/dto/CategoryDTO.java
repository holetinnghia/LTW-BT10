package ltw.bt10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    private Long id;

    @NotBlank(message = "{category.name.notblank}")
    @Size(max = 100, message = "{category.name.size}")
    private String name;

    @Size(max = 255, message = "{category.desc.size}")
    private String description;

    private Boolean active = true;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}