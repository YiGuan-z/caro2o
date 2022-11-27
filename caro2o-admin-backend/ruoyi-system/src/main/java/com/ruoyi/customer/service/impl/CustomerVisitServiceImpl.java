package com.ruoyi.customer.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.customer.mapper.CustomerVisitMapper;
import com.ruoyi.customer.domain.CustomerVisit;
import com.ruoyi.customer.service.ICustomerVisitService;

/**
 * 拜访信息Service业务层处理
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@Service
public class CustomerVisitServiceImpl extends ServiceImpl<CustomerVisitMapper, CustomerVisit> implements ICustomerVisitService {

    /**
     * 查询拜访信息列表
     *
     * @param customerVisit 拜访信息
     * @return 拜访信息
     */
    @Override
    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit) {
        return getBaseMapper().selectCustomerVisitList(customerVisit);
    }

    @Override
    public boolean save(CustomerVisit entity) {
        entity.setInputTime(new Date());
        entity.setInputUser(SecurityUtils.getUserId());
        return super.save(entity);
    }
}
