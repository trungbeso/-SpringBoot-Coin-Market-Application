package com.coinMarket.service;

import com.coinMarket.enums.OrderType;
import com.coinMarket.model.Coin;
import com.coinMarket.model.Order;
import com.coinMarket.model.OrderItem;
import com.coinMarket.model.User;

import java.util.List;

public interface IOrderService {
	Order createOrder(User user, OrderItem orderItem, OrderType orderType);

	Order getOrderById(Long orderId) throws Exception;

	List<Order> getOrdersByUserId(Long userId,OrderType orderType, String assetSymbol);

	Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
