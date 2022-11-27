package com.ruoyi.web.controller.store;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.store.domain.Goods;
import com.ruoyi.store.service.IGoodsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;

import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物品信息Controller
 *
 * @author caseycehng
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/store/goods")
public class GoodsController extends BaseController
{
    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询物品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(Goods goods)
    {
        startPage();
        List<Goods> list = goodsService.selectGoodsList(goods);
        return getDataTable(list);
    }

    /**
     * 导出物品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:goods:export')")
    @Log(title = "物品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Goods goods)
    {
        List<Goods> list = goodsService.selectGoodsList(goods);
        ExcelUtil<Goods> util = new ExcelUtil<Goods>(Goods.class);
        util.exportExcel(response, list, "物品信息数据");
    }

    /**
     * 获取物品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:goods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(goodsService.getById(id));
    }

    /**
     * 新增物品信息
     */
    @PreAuthorize("@ss.hasPermi('system:goods:add')")
    @Log(title = "物品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Goods goods)
    {
        return toAjax(goodsService.save(goods));
    }

    /**
     * 修改物品信息
     */
    @PreAuthorize("@ss.hasPermi('system:goods:edit')")
    @Log(title = "物品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Goods goods)
    {
        return toAjax(goodsService.updateById(goods));
    }

    /**
     * 删除物品信息
     */
    @PreAuthorize("@ss.hasPermi('system:goods:remove')")
    @Log(title = "物品信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(goodsService.removeBatchByIds(Arrays.asList(ids)));
    }
}
