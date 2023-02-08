package com.ecommerence.platform.job;

import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final OrderRepository orderRepository;


    public ScheduledTasks(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void ordersOlderThen10MinutesAutomaticDeclineJob() {

        log.debug("ordersOlderThen10MinutesAutomaticDeclineJob started");

        Optional<List<Order>> ordersOlderThan10Minutes = orderRepository.findOrdersOlderThan10Minutes();

        if (ordersOlderThan10Minutes.get().isEmpty()) {
            log.debug("No orders older than 10 minutes found");
            return;
        }
        ordersOlderThan10Minutes.get().forEach(order -> {

            order.setApproved(false);
            orderRepository.save(order);
            log.debug("Order with id: " + order.getId() + " has been set to not approved");
        });

    }
}
