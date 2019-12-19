package com.example.backend.Service;



//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.example.backend.entity.Goods;
import com.example.backend.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 李东雷
 * @TIME：
 */
@Service
public class GoodsService implements GoodsMapper {


    @Autowired
    GoodsMapper goodsMapper;
    //@Autowired
    //ElasticSearchService elasticSearchService;

    // 插入商品
    //@RabbitListener(queues = "goods.insert")
    public void insertGoods(Goods goods) {
        goodsMapper.insertGoods(goods);
    }
    @Override
    public List<Goods> selectGoodsByName(String goodsname) {
        return goodsMapper.selectGoodsByName(goodsname);
    }
//    //RabbitMQ插入商品    异步
//    @RabbitListener(queues = "goods.insert")
//    public void insertGoodsMQ(Goods goods){
//        System.out.println("rabbitmq插入商品");
//        goodsMapper.insertGoods(goods);
//        //.insertGoods(goods);
//        elasticSearchService.insertGoods(goods);
//    }


    // 首页查找商品 Redis缓存中间件支持
    //@Cacheable(cacheNames = "goods", key = "'index-hots-goods'", unless = "#result == null")
    @Override
    public List<Goods> selectHotGoods() {

        System.out.println("首页查找热卖商品");
        return goodsMapper.selectHotGoods();
    }


    // 按照商品id查找商品

    //@Cacheable(cacheNames = "goods", key = "#goodsid")
    @Override
    public Goods selectGoodsById(Long goodsid) {
        System.out.println("按照商品id查找商品,id为：" + goodsid);
        return goodsMapper.selectGoodsById(goodsid);
    }

    @Override
    public List<Goods> selectGoodsByUsername(String username) {
        return goodsMapper.selectGoodsByUsername(username);
    }


    @Override
    public String selectStorenameByUsername(String username) {
        return goodsMapper.selectStorenameByUsername(username);
    }

    @Override
    public Integer waitAdd() {
        return goodsMapper.waitAdd();
    }

    @Override
    public Integer notOnsell() {
        return goodsMapper.notOnsell();
    }

    @Override
    public void UndercarriageGoods(Long goodsid) {
        goodsMapper.UndercarriageGoods(goodsid);
    }

    @Override
    public void updateGoods(Goods goods) {
        goodsMapper.updateGoods(goods);
    }

    @Override
    public List<Goods> selectWaitVerify() {
        return goodsMapper.selectWaitVerify();
    }

    @Override
    public void goodsVerifyPass(Long goodsid) {
        goodsMapper.goodsVerifyPass(goodsid);
    }

    //@CachePut(cacheNames = "goods",key="'index-hots-goods'")
    @Override
    public void addViews(Long goodsid) {
        goodsMapper.addViews(goodsid);
    }

    //@CachePut(cacheNames = "goods",key="'index-hots-goods'")
    @Override
    public void addSellnums(Long goodsid) {
        System.out.println("销量加1");
        goodsMapper.addSellnums(goodsid);
    }
}
