package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.Customer;
import com.ruoyi.system.domain.CustomerLinkmane;
import com.ruoyi.system.mapper.CustomerLinkmaneMapper;
import com.ruoyi.system.service.ICustomerLinkmaneService;
import com.ruoyi.system.service.ICustomerService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 客户联系人Service业务层处理
 *
 * @author csj
 * @date 2022-11-28
 */
@Service
public class CustomerLinkmaneServiceImpl extends ServiceImpl<CustomerLinkmaneMapper, CustomerLinkmane> implements ICustomerLinkmaneService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ICustomerService customerService;


    /**
     * 查询客户联系人列表
     *
     * @param customerLinkmane 客户联系人
     * @return 客户联系人
     */
    @Override
    public List<CustomerLinkmane> selectCustomerLinkmaneList(CustomerLinkmane customerLinkmane) {
        List<CustomerLinkmane> linkmanes = getBaseMapper().selectCustomerLinkmaneList(customerLinkmane);
        for (CustomerLinkmane customerLinkmaneObj : linkmanes){
            SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
            customerLinkmaneObj.setInputUser(sysUser.getUserId());
            customerLinkmaneObj.setInputUserName(sysUser.getNickName());

        }
        return linkmanes;
    }

    @Override
    public boolean save(CustomerLinkmane entity) {
        String username = SecurityUtils.getUsername();
        entity.setInputUserName(username);
        entity.setInputTime(new Date());
        int insert = getBaseMapper().insert(entity);
        return insert > 0;
    }

    @Override
    public boolean updateById(CustomerLinkmane entity) {
        String username = SecurityUtils.getUsername();
        entity.setInputUserName(username);
        int i = getBaseMapper().updateById(entity);
        return i > 0;
    }
}
