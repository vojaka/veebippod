package ee.kim.veebippod.dto;

import java.io.Serializable;

public record ProductDto(
        String name,
        Double price,
        boolean active,
        Integer stock,
        String description,
        String image,
        Long categoryId
)implements Serializable {
}
