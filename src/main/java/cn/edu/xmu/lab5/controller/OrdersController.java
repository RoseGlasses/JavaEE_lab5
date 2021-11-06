package cn.edu.xmu.lab5.controller;

import cn.edu.xmu.lab5.model.GoodsRetVo;
import cn.edu.xmu.lab5.model.OrdersRetVo;
import cn.edu.xmu.lab5.model.OrdersVo;
import cn.edu.xmu.lab5.model.VoObject;
import cn.edu.xmu.lab5.service.OrdersService;
import cn.edu.xmu.lab5.util.ResponseCode;
import cn.edu.xmu.lab5.util.ResponseUtil;
import cn.edu.xmu.lab5.util.ReturnObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static cn.edu.xmu.lab5.util.Common.getRetObject;
import static cn.edu.xmu.lab5.util.Common.processFieldErrors;

@Api(value = "订单API", tags = "订单API")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/orders", produces = "application/json;charset=UTF-8")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @ApiOperation(value = "新建订单",  produces="application/json")
    @ApiImplicitParams({
    })
    @ApiResponses({
    })
    @PostMapping("")
    public boolean createOrder(@Validated @RequestBody OrdersRetVo ordersRetVo){
        return ordersService.createOrders(ordersRetVo);
    }

    @PostMapping("/rocketmq")
    public boolean createOrderWithRocketMQ(@Validated @RequestBody OrdersRetVo ordersRetVo){
        return ordersService.sendOrderCreateMessage(ordersRetVo);
    }



    @GetMapping("{id}")
    public Object getOrdersById(@PathVariable("id") Long id) {
        ReturnObject<VoObject> returnObject = ordersService.findById(id);
        ResponseCode code = returnObject.getCode();
        switch (code){
            case RESOURCE_ID_NOTEXIST:
                httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
                return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
            case OK:
                OrdersRetVo ordersRetVo = (OrdersRetVo) returnObject.getData().createVo();
                return ResponseUtil.ok(ordersRetVo);
            default:
                return ResponseUtil.fail(code);
        }
    }

}

















