package com.safekeep.storage.controller;

import com.safekeep.storage.model.request.CustomerRequest;
import com.safekeep.storage.model.response.CommonResponse;
import com.safekeep.storage.model.response.CustomerResponse;
import com.safekeep.storage.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> createCustomer (@RequestBody CustomerRequest request){
        CustomerResponse customer = service.createCustomer(request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully creating customer")
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer (
            @RequestParam(name = "name", required = false)String name){
        List<CustomerResponse> customerList = service.getAllCustomer(name);
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully retrieving all the customer/s!")
                .data(customerList)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequest request) {
        CustomerResponse updatedCustomer = service.updateCustomer(id, request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer updated successfully!")
                .data(updatedCustomer)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteCustomer(@PathVariable Long id) {
        service.delete(id);
        CommonResponse<Void> response = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer deleted successfully!")
                .build();
        return ResponseEntity.ok(response);
    }
}
