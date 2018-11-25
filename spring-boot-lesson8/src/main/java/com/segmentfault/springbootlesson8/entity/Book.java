//package com.segmentfault.springbootlesson8.entity;
//
//import javax.persistence.*;
//import java.util.Collection;
//import java.util.Date;
//
///**
// * @author 夸克
// * @date 2018/11/25 15:04
// */
//@Entity
//@Table(name = "books")
//@Access(value = AccessType.FIELD)
//public class Book {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    private String name;
//
//    /**
//     * 书签号码
//     */
//    private String isbn;
//
//    /**
//     * 发布日期
//     */
//    private Date publishDate;
//
//    /**
//     * 多对多的对应关系
//     * 会建立中间表
//     */
//    @ManyToMany(mappedBy = "books")
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
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public Date getPublishDate() {
//        return publishDate;
//    }
//
//    public void setPublishDate(Date publishDate) {
//        this.publishDate = publishDate;
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
