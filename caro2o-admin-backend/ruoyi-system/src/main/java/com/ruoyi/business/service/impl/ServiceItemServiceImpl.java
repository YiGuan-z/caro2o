package com.ruoyi.business.service.impl;


import com.ruoyi.business.domain.ServiceItem;
import com.ruoyi.business.mapper.ServiceItemMapper;
import com.ruoyi.business.query.ServiceItemQuery;
import com.ruoyi.business.service.IServiceItemService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ServiceItemServiceImpl implements IServiceItemService {

    @Resource
    private ServiceItemMapper serviceItemMapper;

    @Override
    public List<ServiceItem> query(ServiceItemQuery qo) {
        return serviceItemMapper.selectForList(qo);
    }


    @Override
    public void save(ServiceItem serviceItem) {
        //对于前台传入的参数,一般需要谨慎处理.
        ServiceItem newObj = new ServiceItem();
        newObj.setName(serviceItem.getName());
        newObj.setOriginalPrice(serviceItem.getOriginalPrice());
        newObj.setDiscountPrice(serviceItem.getDiscountPrice());
        newObj.setCarPackage(serviceItem.getCarPackage());
        newObj.setInfo(serviceItem.getInfo());
        newObj.setCreateTime(new Date());
        newObj.setServiceCatalog(serviceItem.getServiceCatalog());
        //判断是否套餐
        if (ServiceItem.CARPACKAGE_YES.equals(newObj.getCarPackage())) {
            newObj.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);//设置状态为初始化
        } else {
            newObj.setAuditStatus(ServiceItem.AUDITSTATUS_NO_REQUIRED);//设置状态为无需审核
        }
        serviceItemMapper.insert(newObj);
    }

    @Override
    public ServiceItem get(Long id) {
        return serviceItemMapper.selectByPrimaryKey(id);
    }


    @Override
    public void update(ServiceItem serviceItem) {
        ServiceItem oldObj = this.get(serviceItem.getId());
        //处于上架状态，处于审核中的状态不能进行修改.
        if (ServiceItem.SALESTATUS_ON.equals(oldObj.getSaleStatus()) //处于上架状态
                || ServiceItem.AUDITSTATUS_AUDITING.equals(oldObj.getAuditStatus())) {//处于审核中
            throw new BusinessException("非法操作");
        }
        //如果是套餐，如果已经审核通过，然后进行修改,变成初始化
        if (ServiceItem.AUDITSTATUS_APPROVED.equals(oldObj.getAuditStatus())) {
            oldObj.setAuditStatus(ServiceItem.AUDITSTATUS_INIT);
        }
        //把前台的属性设置当当前对象中
        oldObj.setName(serviceItem.getName());
        oldObj.setOriginalPrice(serviceItem.getOriginalPrice());
        oldObj.setDiscountPrice(serviceItem.getDiscountPrice());
        oldObj.setServiceCatalog(serviceItem.getServiceCatalog());
        oldObj.setInfo(serviceItem.getInfo());
        serviceItemMapper.updateByPrimaryKey(oldObj);
    }

    @Override
    public void deleteBatch(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            serviceItemMapper.deleteByPrimaryKey(dictId);
        }
    }

    @Override
    public List<ServiceItem> list() {
        return serviceItemMapper.selectAll();
    }

    @Override
    public void saleOn(Long id) {
        //合理化校验
        ServiceItem oldObj = this.get(id);
        if (oldObj != null) {
            //1.如果处于上架状态,不需要做事情
            if (ServiceItem.SALESTATUS_ON.equals(oldObj.getSaleStatus())) {
                return;
            }
            //2.如果套餐且处于非审核通过,不允许进行上架操作
            if (ServiceItem.CARPACKAGE_YES.equals(oldObj.getCarPackage()) //是套餐
                    && !ServiceItem.AUDITSTATUS_APPROVED.equals(oldObj.getAuditStatus())) {
                throw new BusinessException("未审核通过套餐不允许上架");
            }
            //其他情况都可以进行上架
            serviceItemMapper.updateSaleStatus(id, ServiceItem.SALESTATUS_ON);
        }
    }

    @Override
    public void saleOff(Long id) {
        serviceItemMapper.updateSaleStatus(id, ServiceItem.SALESTATUS_OFF);
    }

    @Override
    public void changeStatus(Long serviceItemId, Integer status) {
        serviceItemMapper.changeAuditStatus(serviceItemId, status);
    }
}
