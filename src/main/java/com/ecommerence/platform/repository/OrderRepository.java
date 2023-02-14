package com.ecommerence.platform.repository;

import com.ecommerence.platform.constants.SqlConstants;
import com.ecommerence.platform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    @Query(value = SqlConstants.GET_ORDERS_FOR_AUTOMATIC_DECLINE, nativeQuery = true)
    Optional<List<Order>> findOrdersOlderThan10Minutes();

    @Query(value = SqlConstants.GET_ORDER_BY_ID_FOR_UPDATE, nativeQuery = true)
    Optional<Order> findByIdForUpdate(@Param("id") Integer id);
}
