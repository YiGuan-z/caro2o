package com.ruoyi.contract.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.contract.domain.ContractItemInfo;

/**
 * 合同项信息Service接口
 *
 * @author xiaochen
 * @date 2022-11-27
 */
public interface IContractItemInfoService extends IService<ContractItemInfo> {

    /**
     * 查询合同项信息列表
     *
     * @param contractItemInfo 合同项信息
     * @return 合同项信息集合
     */
    public List<ContractItemInfo> selectContractItemInfoList(ContractItemInfo contractItemInfo);

}
