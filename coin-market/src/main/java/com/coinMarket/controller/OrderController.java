package com.coinMarket.controller;

import com.coinMarket.enums.OrderType;
import com.coinMarket.model.Coin;
import com.coinMarket.model.Order;
import com.coinMarket.model.User;
import com.coinMarket.request.CreateOrderRequest;
import com.coinMarket.service.ICoinService;
import com.coinMarket.service.IOrderService;
import com.coinMarket.service.IUserService;
import com.coinMarket.service.OrderService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
	IOrderService orderService;
	IUserService userService;
	ICoinService coinService;
	//walletTransactionService

	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
	                                             @RequestBody CreateOrderRequest request) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(request.getCoinId());

		Order order = orderService.processOrder(coin, request.getQuantity() ,request.getOrderType(),user );

		return ResponseEntity.ok(order);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt,
	                                          @PathVariable Long orderId) throws Exception {
//		if (jwt == null) {
//			throw new Exception("Token is missing");
//		}
		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		if (order.getUser().getId().equals(user.getId())) {
			return ResponseEntity.ok(order);
		} else {
			throw new Exception("You dont have permission to access this order");
		}
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader("Authorization") String jwt,
	                                                       @RequestParam(required = false) OrderType orderTpe,
	                                                       @RequestParam(required = false) String assetSymbol) throws Exception {

		Long userId = userService.findUserProfileByJwt(jwt).getId();
		List<Order> userOrders = orderService.getOrdersByUserId(userId, orderTpe, assetSymbol);
		return ResponseEntity.ok(userOrders);
	}
}
