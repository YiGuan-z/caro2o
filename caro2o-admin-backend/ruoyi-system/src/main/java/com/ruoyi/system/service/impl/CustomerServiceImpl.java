package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.mapper.CustomerMapper;
import com.ruoyi.system.domain.Customer;
import com.ruoyi.system.service.ICustomerService;

/**
 * 客户信息Service业务层处理
 *
 * @author csj
 * @date 2022-11-27
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ISysUserService sysUserService;
    /**
     * 查询客户信息列表
     *
     * @param customer 客户信息
     * @return 客户信息
     */
    @Override
    public List<Customer> selectCustomerList(Customer customer) {
        List<Customer> customers = getBaseMapper().selectCustomerList(customer);
        for (Customer customerObj : customers) {
            SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
            customerObj.setInputUser(sysUser.getUserId());
            customerObj.setInputUserName(sysUser.getNickName());
            customerObj.setUserMarket(sysUser.getNickName());
        }
        return customers;
    }

    @Override
    public Customer selectUserById(String name) {
        return customerMapper.selectUserById(name);
    }

    @Override
    public boolean save(Customer entity) {
        String loginUser = SecurityUtils.getUsername();
        entity.setUserMarket(loginUser);
        entity.setInputTime(new Date());
        int insert = getBaseMapper().insert(entity);
        return insert > 0;
    }

    @Override
    public boolean updateById(Customer entity) {
        String loginUser = SecurityUtils.getUsername();
        entity.setInputUserName(loginUser);
        int i = getBaseMapper().updateById(entity);

        return i > 0;
    }


}
