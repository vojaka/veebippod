package ee.kim.veebippod.controller;

import ee.kim.veebippod.entity.Order;
import ee.kim.veebippod.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;


    @GetMapping("orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    //lisamise
    @PostMapping("orders")
    public Order addOrder(@RequestBody Order order) {
        order.setActive(true);
        return orderRepository.save(order);
    }

    //mitte aktiivseks muutmine
    @PatchMapping("orders/{id}/inactive")
    public Order makeOrderInactive(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setActive(false);
        return orderRepository.save(order);
    }

    //yhevaatamine
    @GetMapping("orders/{id}")
    public Order getOneOrder(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

}
