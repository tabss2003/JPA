package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRespository {
    private final EntityManager em;

    // N+1 문제
    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders(); // query 1번 결과가 2개 query 1-> N번
        result.forEach(o->{ // 루프 2번 돌아서
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // query 2번
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /**
     * findOrderQueryDtos() vs findAllByDto_optimization() 차이
     * findOrderQueryDtos() :  findOrders() 쿼리를 한 번 날리고, 루프를 돌릴때마다 쿼리를 날렸는데
     * findAllByDto_optimization() : findOrders() 쿼리를 한 번 날리고 orderItems에서 쿼리 한번, 메모리에서 매칭해서 값을 셋팅해주는것 :쿼리 2번
     */
    public List<OrderQueryDto> findAllByDto_optimization() {

        // 루트 조회(toOne코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();

        //orderItem 컬렉션을 MAP 한방에 조회
        // orderIds -> 2개 뽑힘 이걸 그대로 query에 넣음 
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        // 루프를 돌면서 컬렉션 추가(추가 쿼리 실행 x)
        result.forEach(o->o.setOrderItems((orderItemMap.get(o.getOrderId()))));

        return result;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice,oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();


        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice,oi.count)"+
                        " from OrderItem oi"+
                        " join oi.item i"+
                        " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address) from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id,m.name,o.status,d.address,i.name,oi.orderPrice,oi.count)" +
                        " from Order o"+
                        " join o.member m"+
                        " join o.delivery d"+
                        " join o.orderItems oi"+
                        " join oi.item i ",OrderFlatDto.class)
                .getResultList();
    }
}
