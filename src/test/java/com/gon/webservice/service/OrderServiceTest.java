package com.gon.webservice.service;

import com.gon.webservice.domain.Address;
import com.gon.webservice.domain.Member;
import com.gon.webservice.domain.Order;
import com.gon.webservice.domain.OrderStatus;
import com.gon.webservice.domain.item.Book;
import com.gon.webservice.domain.item.Item;
import com.gon.webservice.exception.NotEnouhtStockException;
import com.gon.webservice.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문(){
        //given
        Member member = createMember("회원1", new Address("서울", "목동", "123-123"));

        Book book = createBook("jpa 공부", 1000, 10);

        int orderCount=2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다", 1000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());

    }

    @Test
    public void 주문취소(){
        //given
        Member member = createMember("회원1", new Address("서울", "목동", "123-123"));
        Item item = createBook("jpa 공부", 1000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL이다", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소시 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    @Test(expected = NotEnouhtStockException.class)
    public void 상품주문_재고수량초과(){
        //given
        Member member = createMember("회원1", new Address("서울", "목동", "123-123"));
        Item item = createBook("jpa 공부", 1000, 10);

        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 예외가 발생해야한다.");
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book(name,price,stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member(name,address);
        em.persist(member);
        return member;
    }
}