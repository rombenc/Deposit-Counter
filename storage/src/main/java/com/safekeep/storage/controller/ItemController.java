package com.safekeep.storage.controller;

import com.safekeep.storage.model.entity.ItemStatus;
import com.safekeep.storage.model.request.ItemRequest;
import com.safekeep.storage.model.response.ItemResponse;
import com.safekeep.storage.model.response.CommonResponse;
import com.safekeep.storage.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<CommonResponse<ItemResponse>> createItem(@RequestBody ItemRequest request) {
        ItemResponse itemResponse = itemService.createItem(request);
        CommonResponse<ItemResponse> response = CommonResponse.<ItemResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created item.")
                .data(itemResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ItemResponse>>> getAllItems(
            @RequestParam Optional<String> customerName,
            @RequestParam Optional<String> status) {
        List<ItemResponse> items = itemService.getAllItems(customerName,
                status.map(ItemStatus::valueOf));
        CommonResponse<List<ItemResponse>> response = CommonResponse.<List<ItemResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully fetched items.")
                .data(items)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CommonResponse<List<ItemResponse>>> getItemsByCustomer(@PathVariable Long customerId) {
        List<ItemResponse> items = itemService.getItemsByCustomer(customerId);
        CommonResponse<List<ItemResponse>> response = CommonResponse.<List<ItemResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully fetched items for customer.")
                .data(items)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CommonResponse<ItemResponse>> updateItemStatus(@PathVariable Long id,
                                                                         @RequestParam ItemStatus newStatus) {
        ItemResponse itemResponse = itemService.updateItemStatus(id, newStatus);
        CommonResponse<ItemResponse> response = CommonResponse.<ItemResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully updated item status.")
                .data(itemResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<CommonResponse<List<ItemResponse>>> getOverdueItems() {
        List<ItemResponse> overdueItems = itemService.getOverdueItems();
        CommonResponse<List<ItemResponse>> response = CommonResponse.<List<ItemResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully fetched overdue items.")
                .data(overdueItems)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ItemResponse>> updateItem(@PathVariable Long id,
                                                                   @RequestBody ItemRequest request) {
        ItemResponse itemResponse = itemService.updateItem(id, request);
        CommonResponse<ItemResponse> response = CommonResponse.<ItemResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully updated item.")
                .data(itemResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        CommonResponse<Void> response = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Successfully deleted item.")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}