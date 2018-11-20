package com.segmentfault.springbootlesson8.entity;

import javax.persistence.*;

/**
 * 客户实体
 * @author 夸克
 * @date 2018/11/21 00:51
 */
@Entity
@Access(value = AccessType.FIELD)// @Access注解，表示是通过字段去生成对应关系 还是在setter getter方法上生成对应关系
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64)
    private String name;

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
}
