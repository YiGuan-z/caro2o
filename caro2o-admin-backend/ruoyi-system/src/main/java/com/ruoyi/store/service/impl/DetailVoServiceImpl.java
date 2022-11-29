package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.domain.DetailVo;
import com.ruoyi.store.mapper.DetailVoMapper;
import com.ruoyi.store.service.IDetailVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DetailVoServiceImpl extends ServiceImpl<DetailVoMapper,DetailVo> implements IDetailVoService {

    @Resource
    private DetailVoMapper detailVoMapper;
    @Override
    public List<DetailVo> selectDetailList(DetailVo detailVo,Long id) {
        return detailVoMapper.selectDetailList(detailVo,id);
    }
}
