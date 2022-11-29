package com.ruoyi.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.DetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出入库单据Service接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface IDetailVoService extends IService<DetailVo> {

    List<DetailVo> selectDetailList(@Param("detailVo") DetailVo detailVo, @Param("id") Long id);
}
