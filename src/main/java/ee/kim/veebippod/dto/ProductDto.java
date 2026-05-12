package ee.kim.veebippod.dto;

public record ProductDto(
        String name,
        Double price,
        boolean active,
        Integer stock,
        String description,
        String image,
        Long categoryId
) {
}
