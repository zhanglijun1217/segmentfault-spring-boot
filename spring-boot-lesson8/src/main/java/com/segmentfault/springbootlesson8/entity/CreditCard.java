package com.segmentfault.springbootlesson8.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 夸克
 * @date 2018/11/21 01:17
 */
@Entity
@Access(value = AccessType.FIELD)
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 128)
    private String number;

    @Column(name = "register_date")
    private Date registerDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
