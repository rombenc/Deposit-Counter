package com.safekeep.storage.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse <T> {
    private int statusCode;
    private String message;
    private T data;
}
