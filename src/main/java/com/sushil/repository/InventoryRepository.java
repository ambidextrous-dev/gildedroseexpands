package com.sushil.repository;

import com.sushil.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {


    @Override
    Optional<Inventory> findById(String id);

    @Modifying
    @Query(value = SQLQueries.updateInventoryQuery)
    int updateItemQuantity(@Param("name") String itemName, @Param("quantity") int quantity);


}
