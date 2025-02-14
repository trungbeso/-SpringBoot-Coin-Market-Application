package com.coinMarket.repositories;

import com.coinMarket.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoinRepository extends JpaRepository<Coin, String> {
}
