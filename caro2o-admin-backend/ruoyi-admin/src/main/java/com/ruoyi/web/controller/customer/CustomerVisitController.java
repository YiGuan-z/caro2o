package com.ruoyi.web.controller.customer;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.customer.domain.CustomerVisit;
import com.ruoyi.customer.service.ICustomerVisitService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 拜访信息Controller
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/customer/visit")
public class CustomerVisitController extends BaseController
{
    @Autowired
    private ICustomerVisitService customerVisitService;

    /**
     * 查询拜访信息列表
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerVisit customerVisit)
    {
        startPage();
        List<CustomerVisit> list = customerVisitService.selectCustomerVisitList(customerVisit);
        return getDataTable(list);
    }

    /**
     * 导出拜访信息列表
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:export')")
    @Log(title = "拜访信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerVisit customerVisit)
    {
        List<CustomerVisit> list = customerVisitService.selectCustomerVisitList(customerVisit);
        ExcelUtil<CustomerVisit> util = new ExcelUtil<CustomerVisit>(CustomerVisit.class);
        util.exportExcel(response, list, "拜访信息数据");
    }

    /**
     * 获取拜访信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(customerVisitService.getById(id));
    }

    /**
     * 新增拜访信息
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:add')")
    @Log(title = "拜访信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerVisit customerVisit)
    {
        return toAjax(customerVisitService.save(customerVisit));
    }

    /**
     * 修改拜访信息
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:edit')")
    @Log(title = "拜访信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerVisit customerVisit)
    {
        return toAjax(customerVisitService.updateById(customerVisit));
    }

    /**
     * 删除拜访信息
     */
    @PreAuthorize("@ss.hasPermi('customer:visit:remove')")
    @Log(title = "拜访信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(customerVisitService.removeBatchByIds(Arrays.asList(ids)));
    }
}
