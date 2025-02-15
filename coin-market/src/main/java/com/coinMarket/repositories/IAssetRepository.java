package com.coinMarket.repositories;

import com.coinMarket.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAssetRepository extends JpaRepository<Asset, Long> {
	List<Asset> findByUserId(Long userId);

	Asset findByUserIdAndCoinId(Long userId, String coinId);
}
