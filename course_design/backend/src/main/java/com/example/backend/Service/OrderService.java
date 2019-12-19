package com.example.backend.Service;

import com.example.backend.entity.Order;
import com.example.backend.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements OrderMapper {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    @Override
    public List<Order> selectAllOrder(String username) {
        return orderMapper.selectAllOrder(username);
    }
}
