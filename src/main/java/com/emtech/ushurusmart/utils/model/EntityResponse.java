package com.emtech.ushurusmart.utils.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponse<T> {
    private int statusCode;
    private String message;
    private T body;
}
