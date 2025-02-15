package com.coinMarket.service;

import com.coinMarket.model.Asset;
import com.coinMarket.model.Coin;
import com.coinMarket.model.User;
import com.coinMarket.repositories.IAssetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetService implements IAssetService {

	IAssetRepository assetRepository;

	@Override
	public Asset createAsset(User user, Coin coin, double quantity) {
		Asset asset = new Asset();
		asset.setUser(user);
		asset.setCoin(coin);
		asset.setQuantity(quantity);
		asset.setBuyPrice(coin.getCurrentPrice());
		return assetRepository.save(asset);
	}

	@Override
	public Asset getAssetById(long assetId) {
		return assetRepository.findById(assetId).orElseThrow(() -> new RuntimeException("Asset not found"));
	}

	@Override
	public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
		return null;
	}

	@Override
	public List<Asset> getUsersAssets(Long userId) {
		return assetRepository.findByUserId(userId);
	}

	@Override
	public Asset updateAsset(Long assetId, double quantity) {
		Asset oldAsset = getAssetById(assetId);
		oldAsset.setQuantity(quantity + oldAsset.getQuantity());
		assetRepository.save(oldAsset);
		return oldAsset;
	}

	@Override
	public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
		return assetRepository.findByUserIdAndCoinId(userId, coinId);
	}

	@Override
	public void deleteAsset(Long assetId) {
		assetRepository.deleteById(assetId);
	}
}
