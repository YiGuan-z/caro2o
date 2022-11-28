package com.ruoyi.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.CustomerLinkmane;

/**
 * 客户联系人Service接口
 *
 * @author csj
 * @date 2022-11-28
 */
public interface ICustomerLinkmaneService extends IService<CustomerLinkmane> {

    /**
     * 查询客户联系人列表
     *
     * @param customerLinkmane 客户联系人
     * @return 客户联系人集合
     */
    public List<CustomerLinkmane> selectCustomerLinkmaneList(CustomerLinkmane customerLinkmane);

}
