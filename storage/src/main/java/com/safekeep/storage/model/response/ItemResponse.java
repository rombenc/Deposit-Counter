package com.safekeep.storage.model.response;

import com.safekeep.storage.model.entity.ItemStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemResponse {

    private Long id;

    private String name;

    private String description;

    private ItemStatus status;

    private LocalDateTime depositDate;

    private LocalDateTime pickupDate;

    private LocalDateTime expectedPickupDate;

    private String categoryName;

    private String ownerName;
}
