package com.ruoyi.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.Customer;

/**
 * 客户信息Service接口
 *
 * @author csj
 * @date 2022-11-27
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 查询客户信息列表
     *
     * @param customer 客户信息
     * @return 客户信息集合
     */
    public List<Customer> selectCustomerList(Customer customer);

}
