package cn.edu.xmu.lab5.mapper;

import cn.edu.xmu.lab5.model.GoodsPo;
import cn.edu.xmu.lab5.model.PriceStockPo;
import cn.edu.xmu.lab5.model.ProductPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

     List<GoodsPo> findGoods(GoodsPo goodsPo);

     List<GoodsPo> findGoodsWithProduct(GoodsPo goodsPo);

     List<ProductPo> findProduct(ProductPo productPo);

     int createGoods(GoodsPo goodsPo);

     int createProduct(ProductPo productPo);

     int updateGoods(GoodsPo goodsPo);

     int deleteGoods(Integer id);

     int deleteProductByGoodsId(Integer id);

     int deleteProduct(Integer id);

     List<PriceStockPo> findEffectPrice(Integer id);
}
