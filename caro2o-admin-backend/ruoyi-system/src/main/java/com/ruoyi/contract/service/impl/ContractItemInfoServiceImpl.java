package com.ruoyi.contract.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
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

    @Override
    public void auditPass(Long id) {
        ContractItemInfo contract = super.getById(id);
        // 如果服务项作废，则提示非法操作
        if(ContractItemInfo.DEFAULTED_USE_REJECT.equals(contract.getNullifyState())) {
            throw new ServiceException("非法操作");
        }

        getBaseMapper().update(contract,
                Wrappers.<ContractItemInfo>lambdaUpdate().set(ContractItemInfo::getAuditState, ContractItemInfo.DEFAULTED_AUDIT_PASSED)
                        .eq(ContractItemInfo::getId, id));

    }

    @Override
    public void reject(Long id) {
        ContractItemInfo contract = super.getById(id);
        // 如果服务项作废，则提示非法操作
        if(ContractItemInfo.DEFAULTED_USE_REJECT.equals(contract.getNullifyState())) {
            throw new ServiceException("非法操作");
        }
        LambdaUpdateWrapper<ContractItemInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(ContractItemInfo::getAuditState, ContractItemInfo.DEFAULTED_AUDIT_REJECT)
                .eq(ContractItemInfo::getId, id);
        getBaseMapper().update(contract, wrapper);
    }

    @Override
    public void confirm(Long id) {
        ContractItemInfo contract = super.getById(id);
        // 如果服务项作废，则提示非法操作
        if(ContractItemInfo.DEFAULTED_USE_REJECT.equals(contract.getNullifyState())) {
            throw new ServiceException("非法操作");
        }
        contract.setAffixSealState(ContractItemInfo.DEFAULTED_CONFIRM_FAILED);
        LambdaUpdateWrapper<ContractItemInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(ContractItemInfo::getAffixSealState, ContractItemInfo.DEFAULTED_CONFIRM_FAILED)
                        .eq(ContractItemInfo::getId, id);
        getBaseMapper().update(contract,wrapper);
    }

    @Override
    public void failure(Long id) {
        ContractItemInfo contract = super.getById(id);
        // 如果服务项作废，则提示非法操作
        if(ContractItemInfo.DEFAULTED_USE_REJECT.equals(contract.getNullifyState())) {
            throw new ServiceException("非法操作");
        }
        getBaseMapper().update(contract, Wrappers.<ContractItemInfo>lambdaUpdate().set(ContractItemInfo::getNullifyState, ContractItemInfo.DEFAULTED_USE_REJECT)
                .eq(ContractItemInfo::getId, id));
    }

    @Override
    public boolean save(ContractItemInfo entity) {
        entity.setInputTime(new Date());
        entity.setInputUser(SecurityUtils.getUserId());
        entity.setAffixSealState(ContractItemInfo.DEFAULTED_CONFIRM_FAILED);
        entity.setAuditState(ContractItemInfo.DEFAULTED_AUDIT_INIT);
        entity.setNullifyState(ContractItemInfo.DEFAULTED_USE_ING);
        return super.save(entity);

    }


}
