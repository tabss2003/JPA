package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
public class OrderSearch {
    private String memberName;//회원 이름
    private OrderStatus orderStatus; //주문 상태 [ORDER,CANCEL]
}
