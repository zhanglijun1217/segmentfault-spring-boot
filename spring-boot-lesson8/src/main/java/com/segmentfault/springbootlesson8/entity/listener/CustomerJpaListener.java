package com.segmentfault.springbootlesson8.entity.listener;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

/**
 * @author 夸克
 * @date 2018/11/25 17:27
 */
public class CustomerJpaListener {

    /**
     * 持久化之前
     * @param source
     */
    @PrePersist
    public void prePersist(Object source){
        System.out.println("@PrePersist" + source);
    }

    /**
     * 持久化之后
     * @param source
     */
    @PostPersist
    public void postPersist(Object source) {
        System.out.println("@PostPersist" + source);
    }
}
