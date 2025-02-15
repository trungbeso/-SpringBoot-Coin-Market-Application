package com.coinMarket.service;

import com.coinMarket.enums.OrderStatus;
import com.coinMarket.enums.OrderType;
import com.coinMarket.model.*;
import com.coinMarket.repositories.IOrderItemRepository;
import com.coinMarket.repositories.IOrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderService implements IOrderService {

	IOrderRepository orderRepository;
	IWalletService walletService;
	IOrderItemRepository orderItemRepository;
	IAssetService assetService;

	@Override
	public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {

		double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();

		Order order = new Order();
		order.setPrice(BigDecimal.valueOf(price));
		order.setTimeStamp(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		order.setUser(user);
		order.setOrderItem(orderItem);
		order.setOrderType(orderType);
		order = orderRepository.save(order);
		return order;
	}

	@Override
	public Order getOrderById(Long orderId) throws Exception {
		return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
	}

	@Override
	public List<Order> getOrdersByUserId(Long userId, OrderType orderType, String assetSymbol) {
		return orderRepository.findByUserId(userId);
	}

	private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCoin(coin);
		orderItem.setQuantity(quantity);
		orderItem.setBuyPrice(buyPrice);
		orderItem.setSellPrice(sellPrice);
		return orderItemRepository.save(orderItem);
	}

	@Transactional
	public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
		if (quantity <= 0) {
			throw new Exception("quantity should be greater than 0");
		}

		double buyPrice = coin.getCurrentPrice();
		OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

		Order order = createOrder(user, orderItem, OrderType.BUY);
		orderItem.setOrder(order);

		walletService.payOrderPayment(order, user);
		order.setStatus(OrderStatus.SUCCESS);
		order.setOrderType(OrderType.BUY);
		Order savedOrder = orderRepository.save(order);

		//create asset
		Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
			  order.getUser().getId(),
			  order.getOrderItem().getCoin().getId());
		if (oldAsset == null) {
			Asset newAsset = assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
		} else {
			assetService.updateAsset(oldAsset.getId(), quantity);
		}

		return savedOrder;
	}

	@Transactional
	public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
		if (quantity <= 0) {
			throw new Exception("quantity should be greater than 0");
		}

		double sellPrice = coin.getCurrentPrice();
		Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
		if (assetToSell != null) {

			double buyPrice = assetToSell.getBuyPrice();
			OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);

			Order order = createOrder(user, orderItem, OrderType.SELL);
			orderItem.setOrder(order);

			if (assetToSell.getQuantity() >= quantity) {

				order.setStatus(OrderStatus.SUCCESS);
				order.setOrderType(OrderType.SELL);
				Order savedOrder = orderRepository.save(order);

				walletService.payOrderPayment(order, user);

				Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), quantity);

				if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
					assetService.deleteAsset(updatedAsset.getId());
				}
				return savedOrder;
			}
			throw new Exception("quantity should be greater than 0");
		}
		throw new Exception("Asset not found");
	}

	@Override
	@Transactional
	public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
		if (orderType.equals(OrderType.BUY)) {
			return buyAsset(coin, quantity, user);
		} else if (orderType.equals(OrderType.SELL)) {
			return sellAsset(coin, quantity, user);
		}
		throw new Exception("Invalid order type");
	}
}
