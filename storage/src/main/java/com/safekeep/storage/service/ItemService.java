package com.safekeep.storage.service;

import com.safekeep.storage.model.entity.*;
import com.safekeep.storage.model.request.ItemRequest;
import com.safekeep.storage.model.response.ItemResponse;
import com.safekeep.storage.repository.CategoryRepository;
import com.safekeep.storage.repository.CustomerRepository;
import com.safekeep.storage.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;

    private static final int MAX_ITEMS_PER_CUSTOMER = 3;
    private static final int MAX_STORAGE_DAYS = 30;

    public ItemResponse createItem(ItemRequest request) {
        Customer owner = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + request.getCustomerId() + " not found."));

        // Check if customer has reached maximum items
        long currentItemCount = itemRepository.countByCustomerAndStatus(owner.getId(), ItemStatus.STORED.toString());
        if (currentItemCount >= MAX_ITEMS_PER_CUSTOMER) {
            throw new IllegalStateException("Customer has reached maximum number of free items (" + MAX_ITEMS_PER_CUSTOMER + ")");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + request.getCategoryId() + " not found."));

        var item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(ItemStatus.STORED)
                .depositDate(LocalDateTime.now())
                .expectedPickupDate(LocalDateTime.now().plusDays(MAX_STORAGE_DAYS))
                .category(category)
                .customer(owner)
                .build();

        return toItemResponse(itemRepository.saveAndFlush(item));
    }

    public List<ItemResponse> getAllItems(Optional<String> customerName, Optional<ItemStatus> status) {
        return itemRepository.findAll().stream()
                .filter(item -> customerName.map(name ->
                                item.getCustomer().getName().toLowerCase().contains(name.toLowerCase()))
                        .orElse(true))
                .filter(item -> status.map(s -> item.getStatus() == s).orElse(true))
                .map(this::toItemResponse)
                .toList();
    }

    public List<ItemResponse> getItemsByCustomer(Long customerId) {
        return itemRepository.findByCustomer(customerId).stream()
                .map(this::toItemResponse)
                .toList();
    }

    public ItemResponse updateItemStatus(Long id, ItemStatus newStatus) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (newStatus == ItemStatus.RETRIEVED) {
            item.setPickupDate(LocalDateTime.now());
            if (LocalDateTime.now().isAfter(item.getExpectedPickupDate())) {
                throw new IllegalStateException("Item is overdue. Please contact administrator.");
            }
        }

        item.setStatus(newStatus);
        return toItemResponse(itemRepository.save(item));
    }

    public List<ItemResponse> getOverdueItems() {
        LocalDateTime now = LocalDateTime.now();
        return itemRepository.findByStatusAndExpectedPickupDateBefore(ItemStatus.STORED.toString(), now)
                .stream()
                .map(this::toItemResponse)
                .toList();
    }

    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));


        if (item.getStatus() != ItemStatus.STORED) {
            throw new IllegalStateException("Cannot update item that is not in storage");
        }

        Customer owner = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(category);
        item.setCustomer(owner);

        return toItemResponse(itemRepository.save(item));
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        // todo: create a logic where only retrieved item can be deleted
        if (item.getStatus() != ItemStatus.RETRIEVED) {
            throw new IllegalStateException("Cannot delete item that hasn't been picked up");
        }

        itemRepository.deleteById(id);
    }

    private ItemResponse toItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .status(item.getStatus())
                .depositDate(item.getDepositDate())
                .pickupDate(item.getPickupDate())
                .expectedPickupDate(item.getExpectedPickupDate())
                .categoryName(item.getCategory().getName())
                .ownerName(item.getCustomer().getName())
                .build();
    }
}