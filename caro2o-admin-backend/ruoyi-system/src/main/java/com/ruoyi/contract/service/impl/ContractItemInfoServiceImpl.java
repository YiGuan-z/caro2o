package com.ruoyi.contract.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.contract.mapper.ContractItemInfoMapper;
import com.ruoyi.contract.domain.ContractItemInfo;
import com.ruoyi.contract.service.IContractItemInfoService;

/**
 * 合同项信息Service业务层处理
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@Service
public class ContractItemInfoServiceImpl extends ServiceImpl<ContractItemInfoMapper, ContractItemInfo> implements IContractItemInfoService {

    /**
     * 查询合同项信息列表
     *
     * @param contractItemInfo 合同项信息
     * @return 合同项信息
     */
    @Override
    public List<ContractItemInfo> selectContractItemInfoList(ContractItemInfo contractItemInfo) {
        return getBaseMapper().selectContractItemInfoList(contractItemInfo);
    }
}
