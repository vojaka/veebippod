package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.OrderDto;
import ee.kim.veebippod.entity.Order;
import ee.kim.veebippod.entity.OrderRow;
import ee.kim.veebippod.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    //lisamise
    @PostMapping("orders")
    public Order addOrder(@RequestBody List<OrderRow> orderRows) {
        return orderService.saveOrder(orderRows);
    }

    //mitte aktiivseks muutmine
    @PatchMapping("orders/{id}/inactive")
    public OrderDto makeOrderInactive(@PathVariable Long id) {
        return orderService.makeOrderInactive(id);
    }

    //yhevaatamine
    @GetMapping("orders/{id}")
    public OrderDto getOneOrder(@PathVariable Long id) {
        return orderService.getOneOrder(id);
    }
}
