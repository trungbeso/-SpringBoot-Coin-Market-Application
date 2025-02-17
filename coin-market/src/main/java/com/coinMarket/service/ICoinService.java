package com.coinMarket.service;

import com.coinMarket.model.Coin;

import java.util.List;

public interface ICoinService {
	List<Coin> getAllCoins(int page) throws Exception;

	String getMarketChart(String coinId, int days) throws Exception;

	String getCoinDetail(String coinId) throws Exception;

	Coin findById(String coinId) throws Exception;

	String searchCoin(String keyword) throws Exception;

	String getTop50CoinsByMarketCapRank() throws Exception;

	String getTrendingCoins() throws Exception;
}
