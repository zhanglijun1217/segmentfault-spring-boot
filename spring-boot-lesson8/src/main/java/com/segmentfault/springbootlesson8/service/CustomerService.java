package com.segmentfault.springbootlesson8.service;

import com.segmentfault.springbootlesson8.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

/**
 * @author 夸克
 * @date 2018/11/25 15:34
 */
@Service
public class CustomerService {


    /**
     * 持久化的上下文
     */
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(rollbackFor = Exception.class)
    public Customer add(Customer customer) {
        entityManager.persist(customer);

        Long id = customer.getId();

        if(Objects.nonNull(id) && id != 0) {
            return selectById(id);
        }

        return null;
    }



    private  Customer selectById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        return customer;
    }
}
