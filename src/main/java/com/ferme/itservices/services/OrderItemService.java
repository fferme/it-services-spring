package com.ferme.itservices.services;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ferme.itservices.dtos.mappers.OrderItemMapper.*;
import static com.ferme.itservices.utils.JsonDataRead.readOrderItemsJsonData;

@Service
@AllArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	@Transactional(readOnly = true)
	public List<OrderItemDTO> listAll() {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		return toOrderItemDTOList(
			orderItems.stream()
				.sorted(Comparator.comparing(OrderItem::getDescription))
				.collect(Collectors.toList())
		);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderItemDTO findById(@Valid @NotNull UUID id) {
		OrderItem orderItem = orderItemRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));

		return toOrderItemDTO(orderItem);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderItemDTO findByDescription(@NotBlank String description) {
		OrderItem orderItem = orderItemRepository.findByDescription(description)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, description));

		return toOrderItemDTO(orderItem);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderItemDTO create(OrderItemDTO orderItemDTO) {
		OrderItem orderItem = toOrderItem(orderItemDTO);

		return toOrderItemDTO(orderItemRepository.save(orderItem));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderItemDTO update(@NotNull UUID id, @Valid @NotNull OrderItemDTO updatedOrderItemDTO) {
		return orderItemRepository.findById(id)
			.map(orderItemFound -> {
				orderItemFound.setOrderItemType(OrderItemTypeConverter.convertToOrderItemType(
					updatedOrderItemDTO.orderItemType().getValue())
				);
				orderItemFound.setDescription(updatedOrderItemDTO.description());
				orderItemFound.setPrice(updatedOrderItemDTO.price());

				return toOrderItemDTO(orderItemRepository.save(orderItemFound));

			}).orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(@NotNull UUID id) {
		orderItemRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll() {
		orderItemRepository.deleteAll();
	}

	@Generated
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OrderItemDTO> exportDataToOrderItem() {
		return toOrderItemDTOList(orderItemRepository.saveAll(readOrderItemsJsonData()));
	}
}