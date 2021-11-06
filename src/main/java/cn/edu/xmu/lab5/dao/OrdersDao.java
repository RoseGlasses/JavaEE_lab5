package cn.edu.xmu.lab5.dao;

import cn.edu.xmu.lab5.mapper.OrdersMapper;
import cn.edu.xmu.lab5.model.*;
import cn.edu.xmu.lab5.util.ReturnObject;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.edu.xmu.lab5.util.Common.genSeqNum;

@Repository
public class OrdersDao {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 创建Orders对象
     * @param orders 传入的orders对象
     */
    public Long createOrders(Orders orders){
        OrdersPo ordersPo = orders.getOrdersPo();
        try{
            ordersMapper.createOrders(ordersPo);
            return ordersPo.getId();
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * 创建OrderItem对象
     * @param orderItem 传入的orderItem对象
     */
    public boolean createOrderItem(List<OrderItem> orderItem){
        for(OrderItem temp:orderItem){
            OrderItemPo orderItemPo=temp.getOrderItemPo();
            try{
                ordersMapper.createOrderItem(orderItemPo);
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }


    /**
     * 用OrdersPo对象找Orders对象
     * @param ordersPo 条件对象，所有条件为AND，仅有索引的值可以作为条件
     * @return  orders对象列表
     */
    public ReturnObject<List<OrdersRetVo>> findOrders(OrdersPo ordersPo){

        List<OrdersPo> ordersPos=ordersMapper.findOrders(ordersPo);

        OrderItemPo orderItemPo=new OrderItemPo();
        orderItemPo.setOrderId(ordersPo.getId());
        List<OrderItemPo> orderItemPos=ordersMapper.findOrderItem(orderItemPo);

        List<OrderItem> orderItems=new ArrayList<>(orderItemPos.size());

        for(OrderItemPo temp:orderItemPos){
            OrderItem item=new OrderItem(temp);
            orderItems.add(item);
        }

        if(ordersPos.size()==1){
            List<OrdersRetVo> retOrdersRetVo=new ArrayList<>(1);
            retOrdersRetVo.add(new OrdersRetVo(new Orders(ordersPos.get(0)),orderItems));
            return new ReturnObject<>(retOrdersRetVo);
        }
        else
            return null;
    }
}


























