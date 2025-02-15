package com.coinMarket.service;

import com.coinMarket.model.Coin;
import com.coinMarket.model.User;
import com.coinMarket.model.Watchlist;
import com.coinMarket.repositories.IWatchlistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WatchlistService implements IWatchlistService{

	IWatchlistRepository watchlistRepository;

	@Override
	public Watchlist findUserWatchlist(Long userId) {
		Watchlist watchlist = watchlistRepository.findByUserId(userId);
		if (watchlist == null) {
			throw new RuntimeException("Watchlist not found");
		}
		return watchlist;
	}

	@Override
	public Watchlist createWatchList(User user) {
		Watchlist watchlist = new Watchlist();
		watchlist.setUser(user);
		watchlist = watchlistRepository.save(watchlist);
		return watchlist;
	}

	@Override
	public Watchlist findById(Long id) {
		Optional<Watchlist> watchlistOpt = watchlistRepository.findById(id);
		if (watchlistOpt.isEmpty()) {
			throw new RuntimeException("Watchlist not found");
		}
		return watchlistOpt.get();
	}

	@Override
	public Coin addItemToWatchList(Coin coin, User user) {
		Watchlist watchlist = findUserWatchlist(user.getId());
		if (watchlist.getCoins().contains(coin)) {
			watchlist.getCoins().remove(coin);
		} else {
			watchlist.getCoins().add(coin);
		}
		watchlistRepository.save(watchlist);
		return coin;
	}
}
