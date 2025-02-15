package com.coinMarket.controller;

import com.coinMarket.model.Asset;
import com.coinMarket.model.User;
import com.coinMarket.service.IAssetService;
import com.coinMarket.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/assets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetController {

	IAssetService assetService;
	IUserService userService;

	@GetMapping("/{assetId}")
	public ResponseEntity<Asset> getAssetById(@PathVariable("assetId") Long assetId) {
		Asset asset = assetService.getAssetById(assetId);
		return ResponseEntity.ok().body(asset);
	}

	@GetMapping("/coin/{coinId}/user")
	public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable String coinId,
	                                                       @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
		return ResponseEntity.ok().body(asset);
	}

	@GetMapping()
	public ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Asset> assetList = assetService.getUsersAssets(user.getId());
		return ResponseEntity.ok().body(assetList);
	}

}
