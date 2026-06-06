package ee.kim.veebippod.service;

import ee.kim.veebippod.dto.OrderDto;
import ee.kim.veebippod.entity.Order;
import ee.kim.veebippod.entity.OrderRow;
import ee.kim.veebippod.entity.Person;
import ee.kim.veebippod.entity.Product;
import ee.kim.veebippod.mapper.OrderMapper;
import ee.kim.veebippod.repository.OrderRepository;
import ee.kim.veebippod.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @CachePut(value = "orderCache", key = "#id")
    public OrderDto makeOrderInactive(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setActive(false);
        orderRepository.save(order);
        return mapToOrderDto(order);
    }

    @Cacheable(value = "orderCache", key = "#id")
    public OrderDto getOneOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToOrderDto(order);
    }

    public Order saveOrder(List<OrderRow> orderRows) {
        Order order = new Order();
        order.setOrderRows(orderRows);
        Person person = new Person();
        person.setId(1L);
        order.setPerson(person);
        order.setActive(true);
        Double total = orderRows
                .stream()
                .map(e -> {
                    OrderRow orderRow = new OrderRow();
                    Product product = productRepository.findById(e.getProduct().getId()).orElseThrow();
                    orderRow.setProduct(product);
                    orderRow.setQuantity(e.getQuantity());
                    decreaseStock(product, e.getQuantity());
                    return orderRow;
                })
                .mapToDouble(e -> e.getProduct().getPrice() * e.getQuantity())
                .sum();
        order.setTotal(total);
        return orderRepository.save(order);
    }

    public void decreaseStock(Product product, int stockChange) {
        if (product.getStock() < stockChange) throw new RuntimeException("Not enough stock");
        product.setStock(product.getStock() - stockChange);
    }

    public void increaseStock(Product product, int stockChange) {
        product.setStock(product.getStock() + stockChange);
    }

    public OrderDto mapToOrderDto(Order order) {
        return orderMapper.mapToOrderDto(order);
    }
}
