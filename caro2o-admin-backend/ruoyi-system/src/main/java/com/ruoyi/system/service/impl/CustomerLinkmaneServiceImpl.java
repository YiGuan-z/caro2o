package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.mapper.CustomerLinkmaneMapper;
import com.ruoyi.system.domain.CustomerLinkmane;
import com.ruoyi.system.service.ICustomerLinkmaneService;

/**
 * 客户联系人Service业务层处理
 *
 * @author csj
 * @date 2022-11-28
 */
@Service
public class CustomerLinkmaneServiceImpl extends ServiceImpl<CustomerLinkmaneMapper, CustomerLinkmane> implements ICustomerLinkmaneService {

    /**
     * 查询客户联系人列表
     *
     * @param customerLinkmane 客户联系人
     * @return 客户联系人
     */
    @Override
    public List<CustomerLinkmane> selectCustomerLinkmaneList(CustomerLinkmane customerLinkmane) {
        return getBaseMapper().selectCustomerLinkmaneList(customerLinkmane);
    }
}
