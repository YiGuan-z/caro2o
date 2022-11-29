package com.ruoyi.web.controller.store;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.store.domain.DetailVo;
import com.ruoyi.store.service.IDetailVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/store/detail")
public class DetailVoController {

    @Autowired
    private IDetailVoService detailVoService;

    @GetMapping("/list/{id}")
    public AjaxResult list(DetailVo detailVo,@PathVariable Long id) {
        List<DetailVo> detailList = detailVoService.selectDetailList(detailVo, id);
        for (int i = 0; i < detailList.size(); i++) {
            detailList.get(i).setId((long) i + 1);
        }
        return AjaxResult.success(detailList);
    }

}
