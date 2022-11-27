package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.workflow.domain.GoodsCategory;
import com.ruoyi.workflow.service.IGoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 物品分类信息Controller
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/workflow/category")
public class GoodsCategoryController extends BaseController
{
    @Autowired
    private IGoodsCategoryService goodsCategoryService;

    /**
     * 查询物品分类信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:list')")
    @GetMapping("/list")
    public AjaxResult list(GoodsCategory goodsCategory)
    {
        List<GoodsCategory> list = goodsCategoryService.selectGoodsCategoryList(goodsCategory);
        return AjaxResult.success(list);
    }

    /**
     * 导出物品分类信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:export')")
    @Log(title = "物品分类信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsCategory goodsCategory)
    {
        List<GoodsCategory> list = goodsCategoryService.selectGoodsCategoryList(goodsCategory);
        ExcelUtil<GoodsCategory> util = new ExcelUtil<GoodsCategory>(GoodsCategory.class);
        util.exportExcel(response, list, "物品分类信息数据");
    }

    /**
     * 获取物品分类信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(goodsCategoryService.getById(id));
    }

    /**
     * 新增物品分类信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:add')")
    @Log(title = "物品分类信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsCategory goodsCategory)
    {
        return toAjax(goodsCategoryService.save(goodsCategory));
    }

    /**
     * 修改物品分类信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:edit')")
    @Log(title = "物品分类信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsCategory goodsCategory)
    {
        return toAjax(goodsCategoryService.updateById(goodsCategory));
    }

    /**
     * 删除物品分类信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:category:remove')")
    @Log(title = "物品分类信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(goodsCategoryService.removeBatchByIds(Arrays.asList(ids)));
    }
}
