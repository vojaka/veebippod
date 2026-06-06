package ee.kim.veebippod.mapper;

import ee.kim.veebippod.dto.OrderDto;
import ee.kim.veebippod.dto.OrderRowDto;
import ee.kim.veebippod.dto.PersonDto;
import ee.kim.veebippod.entity.Order;
import ee.kim.veebippod.entity.OrderRow;
import ee.kim.veebippod.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto mapToOrderDto(Order order);
    PersonDto mapToPersonDto(Person person);
    OrderRowDto mapToOrderRowDto(OrderRow orderRow);
}
