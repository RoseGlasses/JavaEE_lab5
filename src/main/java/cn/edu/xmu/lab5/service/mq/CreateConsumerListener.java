package cn.edu.xmu.lab5.service.mq;

import cn.edu.xmu.lab5.model.OrdersRetVo;
import cn.edu.xmu.lab5.service.OrdersService;
import cn.edu.xmu.lab5.util.JacksonUtil;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "order-create-topic", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "pay-group")
public class CreateConsumerListener implements RocketMQListener<String> {

    @Autowired
    private OrdersService ordersService;

    @Override
    public void onMessage(String message) {
        OrdersRetVo ordersRetVo = JacksonUtil.toObj(message, OrdersRetVo.class);
        ordersService.createOrders(ordersRetVo);
    }
}
