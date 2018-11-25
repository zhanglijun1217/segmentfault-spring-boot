//package com.segmentfault.springbootlesson8.entity;
//
//import javax.persistence.*;
//import java.util.Collection;
//
///**
// * 商店实体
// * @author 夸克
// * @date 2018/11/25 14:47
// */
//@Entity
//@Table(name = "stores")
//@Access(value = AccessType.FIELD)
//public class Store {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    private String name;
//
//    /**
//     * 一对多关系 外键id的生成
//     * 一个store可以对应多个customer
//     */
//    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
//    private Collection<Customer> customers;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Collection<Customer> getCustomers() {
//        return customers;
//    }
//
//    public void setCustomers(Collection<Customer> customers) {
//        this.customers = customers;
//    }
//}
