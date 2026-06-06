package ee.kim.veebippod.dto;

import java.io.Serializable;
import java.util.List;

public record OrderDto (
        Long id,
        PersonDto person,
        List<OrderRowDto> orderRows,
        Double total,
        Boolean active

)implements Serializable { }
