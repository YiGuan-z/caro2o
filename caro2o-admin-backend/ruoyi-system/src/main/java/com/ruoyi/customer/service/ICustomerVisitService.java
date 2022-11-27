package com.ruoyi.customer.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.customer.domain.CustomerVisit;

/**
 * 拜访信息Service接口
 *
 * @author xiaochen
 * @date 2022-11-27
 */
public interface ICustomerVisitService extends IService<CustomerVisit> {

    /**
     * 查询拜访信息列表
     *
     * @param customerVisit 拜访信息
     * @return 拜访信息集合
     */
    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit);

}
