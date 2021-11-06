package cn.edu.xmu.lab5.service;

import cn.edu.xmu.lab5.dao.OrdersDao;
import cn.edu.xmu.lab5.model.*;
import cn.edu.xmu.lab5.util.JacksonUtil;
import cn.edu.xmu.lab5.util.ResponseCode;
import cn.edu.xmu.lab5.util.ReturnObject;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersDao ordersDao;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmqdemo.order-create-topic.delay-level}")
    private int delayLevel;

    @Value("${rocketmqdemo.order-create-topic.timeout}")
    private long timeout;

    /**
     * 新增商品
     * @param ordersRetVo 新商品信息
     */
    @Transactional
    public boolean createOrders(OrdersRetVo ordersRetVo) {
        try{
            Orders orders=new Orders();
            orders.setConsignee(ordersRetVo.getConsignee());
            orders.setRegionID(ordersRetVo.getRegionId());
            orders.setAddress(ordersRetVo.getAddress());
            orders.setMobile(ordersRetVo.getMobile());
            orders.setMessage(ordersRetVo.getMessage());
            orders.setCouponId(ordersRetVo.getCouponId());
            orders.setPresaleId(ordersRetVo.getPresaleId());
            orders.setGrouponId(ordersRetVo.getGrouponId());
            Long orderId=ordersDao.createOrders(orders);

            List<OrderItem> orderItems=new LinkedList<>();
            for(OrderItemRetVo temp:ordersRetVo.getOrderItems()) {
                OrderItem orderItem=new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setGoodsSkuId(temp.getSkuId());
                orderItem.setQuantity(temp.getQuantity());
                orderItem.setCouponActivityId(temp.getCouponActId());
                orderItems.add(orderItem);
            }
            ordersDao.createOrderItem(orderItems);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean sendOrderCreateMessage(OrdersRetVo ordersRetVo){
        try{
            String json= JacksonUtil.toJson(ordersRetVo);
            rocketMQTemplate.asyncSend("order-create-topic", MessageBuilder.withPayload(json).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    //System.out.println(ordersRetVo+"created successfully!");
                }

                @Override
                public void onException(Throwable throwable) {
                    //System.out.println(ordersRetVo+"creation failed!");
                }
            });
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * 获取某个订单信息，仅展示相关内容
     *
     * @param id 订单id
     * @return 订单对象
     */
    public ReturnObject<VoObject> findById(Long id) {
        OrdersPo queryObj1 = new OrdersPo();
        queryObj1.setId(id);

        ReturnObject<VoObject> retOrders=null;
        ReturnObject<List<OrdersRetVo>> returnObject=ordersDao.findOrders(queryObj1);

        if (returnObject.getCode().equals(ResponseCode.OK)) {
            if (returnObject.getData().size() == 1) {
                retOrders = new ReturnObject<>(returnObject.getData().get(0));
            }else{
                retOrders = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }else{
            retOrders = new ReturnObject<>(returnObject.getCode(), returnObject.getErrmsg());
        }

        return retOrders;
    }
}


























