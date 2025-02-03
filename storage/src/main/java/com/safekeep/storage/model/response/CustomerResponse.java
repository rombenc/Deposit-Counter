package com.safekeep.storage.model.response;

import com.safekeep.storage.model.entity.Item;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String contact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Item> items;
}
