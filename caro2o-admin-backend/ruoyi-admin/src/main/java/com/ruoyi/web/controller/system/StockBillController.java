package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.store.domain.StockBillItem;
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
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.service.IStockBillService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 出入库单据Controller
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/store/bill")
public class StockBillController extends BaseController
{
    @Autowired
    private IStockBillService stockBillService;

    /**
     * 查询出入库单据列表
     */
    @PreAuthorize("@ss.hasPermi('store:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockBill stockBill)
    {
        startPage();
        List<StockBill> list = stockBillService.selectStockBillList(stockBill);
        return getDataTable(list);
    }

    /**
     * 导出出入库单据列表
     */
    @PreAuthorize("@ss.hasPermi('store:bill:export')")
    @Log(title = "出入库单据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockBill stockBill)
    {
        List<StockBill> list = stockBillService.selectStockBillList(stockBill);
        ExcelUtil<StockBill> util = new ExcelUtil<StockBill>(StockBill.class);
        util.exportExcel(response, list, "出入库单据数据");
    }

    /**
     * 获取出入库单据详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:bill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(stockBillService.getById(id));
    }

    /**
     * 新增出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:add')")
    @Log(title = "出入库单据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockBill stockBill)
    {
        return toAjax(stockBillService.save(stockBill));
    }

    /**
     * 修改出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:edit')")
    @Log(title = "出入库单据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockBill stockBill)
    {
        return toAjax(stockBillService.updateById(stockBill));
    }

    /**
     * 删除出入库单据
     */
    @PreAuthorize("@ss.hasPermi('store:bill:remove')")
    @Log(title = "出入库单据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        stockBillService.removeBatchByIds(Arrays.asList(ids));
        return AjaxResult.success(ids[0]);
    }

    @PreAuthorize("@ss.hasPermi('store:bill:remove')")
    @Log(title = "出入库单据", businessType = BusinessType.DELETE)
    @DeleteMapping("/cancellation/{id}")
    public AjaxResult updateStatus(@PathVariable Long id)
    {
        stockBillService.updateStatusById(id);
        return AjaxResult.success("successs");
    }
}
