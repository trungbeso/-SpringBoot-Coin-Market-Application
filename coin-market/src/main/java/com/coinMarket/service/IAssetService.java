package com.coinMarket.service;

import com.coinMarket.model.Asset;
import com.coinMarket.model.Coin;
import com.coinMarket.model.User;

import java.util.List;

public interface IAssetService {
	Asset createAsset(User user, Coin coin, double quantity);

	Asset getAssetById(long assetId);

	Asset getAssetByUserIdAndId(Long userId, Long assetId);

	List<Asset> getUsersAssets(Long userId);

	Asset updateAsset(Long assetId, double quantity);

	Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

	void deleteAsset(Long assetId);
}
