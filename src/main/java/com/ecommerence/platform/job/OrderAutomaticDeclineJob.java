package com.ecommerence.platform.job;

import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.entity.OrderProduct;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.repository.OrderRepository;
import com.ecommerence.platform.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "job.order.automatic.decline.enabled", havingValue = "true", matchIfMissing = true)
public class OrderAutomaticDeclineJob {

    private static final Logger log = LogManager.getLogger(OrderAutomaticDeclineJob.class);


    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderAutomaticDeclineJob(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Scheduled(cron = "${job.order.automatic.decline.cron:0 0/1 * * * *}")
    @Transactional
    public void ordersOlderThen10MinutesAutomaticDeclineJob() {

        log.debug("ordersOlderThen10MinutesAutomaticDeclineJob started");

        Optional<List<Order>> ordersOlderThan10Minutes = orderRepository.findOrdersOlderThan10MinutesForUpdate();

        if (ordersOlderThan10Minutes.get().isEmpty()) {
            log.debug("No orders older than 10 minutes found");
            return;
        }
        ordersOlderThan10Minutes.get().forEach(order -> {

            order.setApproved(false);

            List<Integer> productIdsForOrderDecline = order.getOrderProducts()
                    .stream()
                    .map(OrderProduct::getProduct)
                    .map(Product::getId)
                    .collect(Collectors.toList());


            Map<Integer, Product> selectedProductEntitiesMap = productRepository.findAllByIdsForUpdate(productIdsForOrderDecline)
                    .stream().collect(Collectors.toMap(Product::getId, Function.identity()));


            for(OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = selectedProductEntitiesMap.get(orderProduct.getProduct().getId());
                product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
            }

            orderRepository.save(order);
            productRepository.saveAll(selectedProductEntitiesMap.values());

            log.debug("Order with id: {} has been set to not approved", + order.getId());
        });

    }
}
