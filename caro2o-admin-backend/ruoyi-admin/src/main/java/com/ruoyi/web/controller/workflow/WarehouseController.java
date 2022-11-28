package com.ruoyi.web.controller.workflow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.store.domain.Warehouse;
import com.ruoyi.store.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 仓库信息Controller
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/workflow/warehouse")
public class WarehouseController extends BaseController
{
    @Autowired
    private IWarehouseService warehouseService;

    /**
     * 查询仓库信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(Warehouse warehouse)
    {
        startPage();
        List<Warehouse> list = warehouseService.selectWarehouseList(warehouse);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('workflow:warehouse:list')")
    @GetMapping("/listAll")
    public TableDataInfo list()
    {
        List<Warehouse> list = warehouseService.list();
        return getDataTable(list);
    }

    /**
     * 导出仓库信息列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:export')")
    @Log(title = "仓库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Warehouse warehouse)
    {
        List<Warehouse> list = warehouseService.selectWarehouseList(warehouse);
        ExcelUtil<Warehouse> util = new ExcelUtil<Warehouse>(Warehouse.class);
        util.exportExcel(response, list, "仓库信息数据");
    }

    /**
     * 获取仓库信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(warehouseService.getById(id));
    }

    /**
     * 新增仓库信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:add')")
    @Log(title = "仓库信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Warehouse warehouse)
    {
        return toAjax(warehouseService.save(warehouse));
    }

    /**
     * 修改仓库信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:edit')")
    @Log(title = "仓库信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Warehouse warehouse)
    {
        return toAjax(warehouseService.updateById(warehouse));
    }

    /**
     * 删除仓库信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:warehouse:remove')")
    @Log(title = "仓库信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(warehouseService.removeBatchByIds(Arrays.asList(ids)));
    }
}
