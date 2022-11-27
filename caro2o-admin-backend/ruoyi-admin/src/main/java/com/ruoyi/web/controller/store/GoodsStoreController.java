package com.ruoyi.web.controller.store;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
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
import com.ruoyi.store.domain.GoodsStore;
import com.ruoyi.store.service.IGoodsStoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物品库存Controller
 *
 * @author caseycheng
 * @date 2022-11-27
 */
@RestController
@RequestMapping("/store/store")
public class GoodsStoreController extends BaseController {
	@Autowired
	private IGoodsStoreService goodsStoreService;
	
	/**
	 * 查询所有仓库
	 */
	@GetMapping("/all/store")
	@PreAuthorize("@ss.hasPermi('store:store:list')")
	public AjaxResult getAllStore() {
		Map<String,Map<String,Object>> ret = goodsStoreService.selectAllStore();
		return AjaxResult.success(ret);
	}
	
	/**
	 * 查询物品库存列表
	 */
	@PreAuthorize("@ss.hasPermi('store:store:list')")
	@GetMapping("/list")
	public TableDataInfo list(GoodsStore goodsStore) {
		startPage();
		List<GoodsStore> list = goodsStoreService.selectGoodsStoreList(goodsStore);
		return getDataTable(list);
	}
	
	/**
	 * 导出物品库存列表
	 */
	@PreAuthorize("@ss.hasPermi('store:store:export')")
	@Log(title = "物品库存", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, GoodsStore goodsStore) {
		List<GoodsStore> list = goodsStoreService.selectGoodsStoreList(goodsStore);
		ExcelUtil<GoodsStore> util = new ExcelUtil<GoodsStore>(GoodsStore.class);
		util.exportExcel(response, list, "物品库存数据");
	}
	
	/**
	 * 获取物品库存详细信息
	 */
	@PreAuthorize("@ss.hasPermi('store:store:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") String id) {
		return AjaxResult.success(goodsStoreService.getById(id));
	}
	
	/**
	 * 新增物品库存
	 */
	@PreAuthorize("@ss.hasPermi('store:store:add')")
	@Log(title = "物品库存", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@RequestBody GoodsStore goodsStore) {
		return toAjax(goodsStoreService.save(goodsStore));
	}
	
	/**
	 * 修改物品库存
	 */
	@PreAuthorize("@ss.hasPermi('store:store:edit')")
	@Log(title = "物品库存", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@RequestBody GoodsStore goodsStore) {
		return toAjax(goodsStoreService.updateById(goodsStore));
	}
	
	/**
	 * 删除物品库存
	 */
	@PreAuthorize("@ss.hasPermi('store:store:remove')")
	@Log(title = "物品库存", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable String[] ids) {
		return toAjax(goodsStoreService.removeBatchByIds(Arrays.asList(ids)));
	}
}
