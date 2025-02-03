package com.safekeep.storage.service;

import com.safekeep.storage.exception.CustomerNotFoundException;
import com.safekeep.storage.model.entity.Customer;
import com.safekeep.storage.model.request.CustomerRequest;
import com.safekeep.storage.model.response.CustomerResponse;
import com.safekeep.storage.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerResponse createCustomer (CustomerRequest request){
        var customer = Customer.builder()
                .name(request.getName())
                .contact(request.getContact())
                .createdAt(LocalDateTime.now())
                .build();
        return toCustomerResponse(repository.saveAndFlush(customer));
    }

    public List<CustomerResponse> getAllCustomer (String name) {
        if (name != null) {
            return repository.findAllByNameLikeOrderByNameAsc("%" + name + "%")
                    .stream()
                    .map(this::toCustomerResponse)
                    .toList();
        }
        return repository.findAll().stream().map(this::toCustomerResponse).toList();
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer tidak ditemukan"));

        customer.setName(request.getName());
        customer.setContact(request.getContact());

        return toCustomerResponse(repository.saveAndFlush(customer));
    }

    public void delete(Long id) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found.");
        }
    }

        private CustomerResponse toCustomerResponse (Customer customer){
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contact(customer.getContact())
                .createdAt(customer.getCreatedAt())
                .build();
    }

}
