package com.coinMarket.repositories;

import com.coinMarket.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWatchlistRepository extends JpaRepository<Watchlist, Long> {

	Watchlist findByUserId(Long userId);

}
