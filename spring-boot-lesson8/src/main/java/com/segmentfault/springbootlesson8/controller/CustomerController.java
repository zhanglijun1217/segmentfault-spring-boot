package com.segmentfault.springbootlesson8.controller;

import com.segmentfault.springbootlesson8.entity.Customer;
import com.segmentfault.springbootlesson8.repository.CustomerRepository;
import com.segmentfault.springbootlesson8.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 夸克
 * @date 2018/11/25 15:32
 */
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping(value = "/add")
    public Customer addCustomer(@RequestBody Customer customer) {

        return customerService.add(customer);
    }

    /**
     * =================== repository 操作 ====================
     */
    @Resource
    private CustomerRepository customerRepository;

    @GetMapping(path = "/findAll")
    public List<Customer> findAll() {
        List<Customer> all = customerRepository.findAll();
        return all;
    }

    @GetMapping(path = "/findById/{id}")
    public Customer findById(@PathVariable(value = "id") Long id) {
        return customerRepository.findOne(id);
    }
}
