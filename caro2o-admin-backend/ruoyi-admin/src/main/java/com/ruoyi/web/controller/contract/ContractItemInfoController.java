package com.ruoyi.web.controller.contract;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.web.controller.common.CommonController;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.contract.domain.ContractItemInfo;
import com.ruoyi.contract.service.IContractItemInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 合同项信息Controller
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/contract/itemInfo")
public class ContractItemInfoController extends BaseController
{
    @Autowired
    private IContractItemInfoService contractItemInfoService;

    @Autowired
    @Lazy
    private CommonController commonControllerService;

    /**
     * 查询合同项信息列表
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractItemInfo contractItemInfo)
    {
        startPage();
        List<ContractItemInfo> list = contractItemInfoService.selectContractItemInfoList(contractItemInfo);
        return getDataTable(list);
    }

    /**
     * 导出合同项信息列表
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:export')")
    @Log(title = "合同项信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractItemInfo contractItemInfo)
    {
        List<ContractItemInfo> list = contractItemInfoService.selectContractItemInfoList(contractItemInfo);
        ExcelUtil<ContractItemInfo> util = new ExcelUtil<ContractItemInfo>(ContractItemInfo.class);
        util.exportExcel(response, list, "合同项信息数据");
    }

    /**
     * 获取合同项信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(contractItemInfoService.getById(id));
    }

    /**
     * 新增合同项信息
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:add')")
    @Log(title = "合同项信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractItemInfo contractItemInfo)
    {
        return toAjax(contractItemInfoService.save(contractItemInfo));
    }

    /**
     * 修改合同项信息
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:edit')")
    @Log(title = "合同项信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractItemInfo contractItemInfo)
    {
        return toAjax(contractItemInfoService.updateById(contractItemInfo));
    }

    /**
     * 删除合同项信息
     */
    @PreAuthorize("@ss.hasPermi('contract:itemInfo:remove')")
    @Log(title = "合同项信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(contractItemInfoService.removeBatchByIds(Arrays.asList(ids)));
    }


    @PatchMapping("/pass/{id}")
    public AjaxResult auditPass(@PathVariable Long id) {
        contractItemInfoService.auditPass(id);
        return AjaxResult.success("success");
    }

    @PatchMapping("/reject/{id}")
    public AjaxResult reject(@PathVariable Long id) {
        contractItemInfoService.reject(id);
        return AjaxResult.success("success");
    }

    @PatchMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        contractItemInfoService.confirm(id);
        return AjaxResult.success("success");
    }
    @PatchMapping("/failure/{id}")
    public AjaxResult failure(@PathVariable Long id) {
        contractItemInfoService.failure(id);
        return AjaxResult.success("success");
    }

}
