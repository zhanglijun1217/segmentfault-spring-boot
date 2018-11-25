package com.segmentfault.springbootlesson8.entity;

import com.segmentfault.springbootlesson8.entity.listener.CustomerJpaListener;

import javax.persistence.*;

/**
 * 客户实体
 * @author 夸克
 * @date 2018/11/21 00:51
 */
@Entity
@Access(value = AccessType.FIELD)// @Access注解，表示是通过字段去生成对应关系 还是在setter getter方法上生成对应关系
@Table(name = "customers")
@EntityListeners(value = {CustomerJpaListener.class})
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64)
    private String name;

    /**
     * 一对一关系的信用卡实体
     * 当有对应关系的时候，如果传入参数有级联对象，要设置CascadeType级联的关系。
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    private CreditCard creditCard;

//    /**
//     * 多对一的实体关系
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id", referencedColumnName = "id")
//    private Store store;

    /**
     * 多对多的图书关系
     */
//    @ManyToMany
//    private Collection<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
//
//    public Store getStore() {
//        return store;
//    }
//
//    public void setStore(Store store) {
//        this.store = store;
//    }
//
//    public Collection<Book> getBooks() {
//        return books;
//    }
//
//    public void setBooks(Collection<Book> books) {
//        this.books = books;
//    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creditCard=" + creditCard +
                '}';
    }
}
