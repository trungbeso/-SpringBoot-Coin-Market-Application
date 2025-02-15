package com.coinMarket.controller;

import com.coinMarket.model.Coin;
import com.coinMarket.model.User;
import com.coinMarket.model.Watchlist;
import com.coinMarket.service.ICoinService;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.IWatchlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/watchlist")
public class WatchlistController {

	IWatchlistService watchlistService;
	IUserService userService;
	ICoinService coinService;

	@GetMapping("/user")
	public ResponseEntity<Watchlist> getUserWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
		return ResponseEntity.ok(watchlist);
	}

	@PostMapping("/{watchlistId}")
	public ResponseEntity<Watchlist> getWatchlistById(@PathVariable Long watchlistId){
		Watchlist watchlist = watchlistService.findById(watchlistId);
		return ResponseEntity.ok(watchlist);
	}

	@PatchMapping("/add/coin/{coinId}")
	public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String jwt,
	                                                    @PathVariable String coinId) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(coinId);
		Coin addedCoin = watchlistService.addItemToWatchList(coin, user);
		return ResponseEntity.ok(addedCoin);
	}
}
