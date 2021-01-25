package com.sushil.repository;

import com.sushil.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    @Override
    List<Item> findAll();

    @Override
    Optional<Item> findById(String name);
}
