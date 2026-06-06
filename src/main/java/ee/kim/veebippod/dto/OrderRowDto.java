package ee.kim.veebippod.dto;

import java.io.Serializable;

public record OrderRowDto(
        Integer quantity,
        ProductDto product
) implements Serializable { }
