package cn.edu.xmu.lab5.mapper;

import cn.edu.xmu.lab5.model.OrderItemPo;
import cn.edu.xmu.lab5.model.OrdersPo;
import cn.edu.xmu.lab5.model.OrdersRetVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrdersMapper {

    List<OrdersPo> findOrders(OrdersPo ordersPo);

    List<OrderItemPo> findOrderItem(OrderItemPo orderItemPo);

    void createOrders(OrdersPo ordersPo);

    void createOrderItem(OrderItemPo orderItemPo);

    List<OrdersRetVo> findOrdersWithOrderItem(OrdersPo ordersPo);
}
