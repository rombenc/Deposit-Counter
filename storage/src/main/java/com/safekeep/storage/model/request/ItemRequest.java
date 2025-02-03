package com.safekeep.storage.model.request;

import com.safekeep.storage.model.entity.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {
    private String name;

    private String description;

    private ItemStatus status;

    private LocalDateTime depositDate;

    private LocalDateTime pickupDate;

    private Long categoryId;

    private Long customerId;

}
