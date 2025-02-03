package com.safekeep.storage.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse{
    private Long id;
    private String name;
}
