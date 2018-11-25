package com.segmentfault.springbootlesson8.repository;

import com.segmentfault.springbootlesson8.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * @author 夸克
 * @date 2018/11/25 18:23
 */
@Repository
public class CustomerRepository extends SimpleJpaRepository<Customer, Long> {



    // 利用根据构造函数注入
    @Autowired
    public CustomerRepository(EntityManager entityManager) {
        super(Customer.class, entityManager);
    }

}
