package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;
import com.ruoyi.store.service.IStockBillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 出入库单据Controller
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/store/billItem")
public class StockBillItemController extends BaseController
{
    @Autowired
    private IStockBillItemService stockBillItemService;

    /**
     * 查询出入库单据列表
     */
    @PreAuthorize("@ss.hasPermi('store:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        List<StockBillItem> list = stockBillItemService.list();
        return getDataTable(list);
    }


    /**
     * 获取出入库单据详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:bill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(stockBillItemService.getByBillId(id));
    }

    /**
     * 新增出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:add')")
    @Log(title = "出入库单据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockBillItem stockBillItem)
    {
        return toAjax(stockBillItemService.save(stockBillItem));
    }

    /**
     * 修改出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:edit')")
    @Log(title = "出入库单据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockBillItem stockBillItem)
    {
        return toAjax(stockBillItemService.updateById(stockBillItem));
    }

    /**
     * 删除出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:remove')")
    @Log(title = "出入库单据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        stockBillItemService.removeBatchByIds(Arrays.asList(ids));
        return AjaxResult.success(ids[0]);
    }
}
