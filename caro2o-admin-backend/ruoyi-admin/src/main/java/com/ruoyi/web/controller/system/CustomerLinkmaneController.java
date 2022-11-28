package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.CustomerLinkmane;
import com.ruoyi.system.service.ICustomerLinkmaneService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户联系人Controller
 *
 * @author csj
 * @date 2022-11-28
 */
@RestController
@RequestMapping("/system/linkmane")
public class CustomerLinkmaneController extends BaseController
{
    @Autowired
    private ICustomerLinkmaneService customerLinkmaneService;

    /**
     * 查询客户联系人列表
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerLinkmane customerLinkmane)
    {
        startPage();
        List<CustomerLinkmane> list = customerLinkmaneService.selectCustomerLinkmaneList(customerLinkmane);
        return getDataTable(list);
    }

    /**
     * 导出客户联系人列表
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:export')")
    @Log(title = "客户联系人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerLinkmane customerLinkmane)
    {
        List<CustomerLinkmane> list = customerLinkmaneService.selectCustomerLinkmaneList(customerLinkmane);
        ExcelUtil<CustomerLinkmane> util = new ExcelUtil<CustomerLinkmane>(CustomerLinkmane.class);
        util.exportExcel(response, list, "客户联系人数据");
    }

    /**
     * 获取客户联系人详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(customerLinkmaneService.getById(id));
    }

    /**
     * 新增客户联系人
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:add')")
    @Log(title = "客户联系人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerLinkmane customerLinkmane)
    {
        return toAjax(customerLinkmaneService.save(customerLinkmane));
    }

    /**
     * 修改客户联系人
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:edit')")
    @Log(title = "客户联系人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerLinkmane customerLinkmane)
    {
        return toAjax(customerLinkmaneService.updateById(customerLinkmane));
    }

    /**
     * 删除客户联系人
     */
    @PreAuthorize("@ss.hasPermi('system:linkmane:remove')")
    @Log(title = "客户联系人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(customerLinkmaneService.removeBatchByIds(Arrays.asList(ids)));
    }
}
