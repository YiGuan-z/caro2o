package com.ruoyi.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.DetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetailVoMapper extends BaseMapper<DetailVo> {
    List<DetailVo> selectDetailList(@Param("detailVo") DetailVo detailVo, @Param("id") Long id);
}
