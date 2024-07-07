package com.ferme.itservices.api.orderItem.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ferme.itservices.api.application.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.orderItem.dtos.OrderItemDTO;
import com.ferme.itservices.api.orderItem.dtos.mappers.OrderItemMapper;
import com.ferme.itservices.api.orderItem.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.orderItem.models.OrderItem;
import com.ferme.itservices.api.orderItem.repositories.OrderItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ferme.itservices.api.application.utils.JsonDataRead.readJsonData;
import static com.ferme.itservices.api.orderItem.dtos.mappers.OrderItemMapper.toOrderItemDTOList;

@Service
@AllArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "orderItemsList")
	public List<OrderItemDTO> listAll() {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		return toOrderItemDTOList(
			orderItems.stream()
				.sorted(Comparator.comparing(OrderItem::getDescription))
				.collect(Collectors.toList())
		);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "orderItem", key = "#id")
	public OrderItemDTO findById(@Valid @NotNull UUID id) {
		OrderItem orderItem = orderItemRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));

		return OrderItemMapper.toOrderItemDTO(orderItem);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "orderItem", key = "#description")
	public OrderItemDTO findByDescription(@NotBlank String description) {
		OrderItem orderItem = orderItemRepository.findByDescription(description)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, description));

		return OrderItemMapper.toOrderItemDTO(orderItem);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "orderItemsList", allEntries = true)
	@CachePut(value = "orderItem", key = "#result.id")
	public OrderItemDTO create(OrderItemDTO orderItemDTO) {
		OrderItem orderItem = OrderItemMapper.toOrderItem(orderItemDTO);

		return OrderItemMapper.toOrderItemDTO(orderItemRepository.save(orderItem));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "orderItemsList", allEntries = true)
	@CachePut(value = "orderItem", key = "#id")
	public OrderItemDTO update(@NotNull UUID id, @Valid @NotNull OrderItemDTO updatedOrderItemDTO) {
		return orderItemRepository.findById(id)
			.map(orderItemFound -> {
				orderItemFound.setOrderItemType(OrderItemTypeConverter.convertToOrderItemType(
					updatedOrderItemDTO.orderItemType().getValue())
				);
				orderItemFound.setDescription(updatedOrderItemDTO.description());
				orderItemFound.setPrice(updatedOrderItemDTO.price());

				return OrderItemMapper.toOrderItemDTO(orderItemRepository.save(orderItemFound));

			}).orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"orderItem", "orderItemsList"}, key = "#id")
	public void deleteById(@NotNull UUID id) {
		orderItemRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll() {
		orderItemRepository.deleteAll();
	}

	@Generated
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"orderItem", "orderItemsList"}, allEntries = true)
	public List<OrderItemDTO> exportDataToOrderItem() {
		List<OrderItem> orderItems = orderItemRepository.saveAll(
			readJsonData("src/main/resources/entities/orderItems.json", new TypeReference<List<OrderItem>>() { })
		);
		return toOrderItemDTOList(orderItems);
	}
}