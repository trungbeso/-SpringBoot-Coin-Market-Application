package com.coinMarket.service;

import com.coinMarket.model.Coin;
import com.coinMarket.model.User;
import com.coinMarket.model.Watchlist;

public interface IWatchlistService {
	Watchlist findUserWatchlist(Long userId);

	Watchlist createWatchList(User user);

	Watchlist findById(Long id);

	Coin addItemToWatchList(Coin coin, User user);
}
