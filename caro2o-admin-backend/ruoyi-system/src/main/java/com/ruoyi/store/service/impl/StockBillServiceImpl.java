package com.ruoyi.store.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.builder.Builder;
import com.ruoyi.store.domain.GoodsStore;
import com.ruoyi.store.domain.StockBillItem;
import com.ruoyi.store.mapper.GoodsMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.store.mapper.GoodsStoreMapper;
import com.ruoyi.store.service.IGoodsStoreService;
import com.ruoyi.store.service.IStockBillItemService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.mapper.StockBillMapper;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.service.IStockBillService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出入库单据Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Service
public class StockBillServiceImpl extends ServiceImpl<StockBillMapper, StockBill> implements IStockBillService {
	@Autowired
	private IStockBillItemService stockBillItemService;

	@Autowired
	private GoodsStoreMapper goodsStoreMapper;
	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 查询出入库单据列表
	 *
	 * @param stockBill 出入库单据
	 * @return 出入库单据
	 */
	@Override
	public List<StockBill> selectStockBillList(StockBill stockBill) {
		//还需要查询出录入人
		Long count = getBaseMapper().selectForCount(stockBill);
		if (count == 0) {
			return Collections.emptyList();
		}
		List<StockBill> stockBills = getBaseMapper().selectForList(stockBill);
//		stockBills.forEach(node -> {
//			node.setPrice(node.getPrice().multiply(new BigDecimal(node.getAmounts())));
//			final Long id = node.getOperatorId();
//			node.setUser(sysUserService.selectUserById(id));
//		});
		return stockBills;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateStatusById(Long id) {
		StockBill bill = super.getById(id);
		Integer type = bill.getType();
		if (0 == type) {
//            List<Integer> collect = stockBillItemService.getByBillId(bill.getId()).stream().map(StockBillItem::getAmounts).collect(Collectors.toList());
			// 出入库详细表
			List<StockBillItem> byBillList = stockBillItemService.getByBillId(bill.getId());
			//所有物品id
			List<String> goodIds = byBillList.stream().map(StockBillItem::getGoodsId).collect(Collectors.toList());


			Map<String, StockBillItem> map = byBillList.stream().collect(Collectors.toMap(StockBillItem::getGoodsId, r -> r));
			LambdaQueryWrapper<GoodsStore> wa = new LambdaQueryWrapper<>();
			wa.eq(GoodsStore::getStoreId, bill.getStoreId());
			List<GoodsStore> goodsStores = goodsStoreMapper.selectList(wa);
			goodsStores.forEach(
					n -> {
						StockBillItem stockBillItem = map.get(n.getGoodsId());
						n.setAmounts(n.getAmounts() - stockBillItem.getAmounts());
					}
			);
			//删除goodsStore表
			LambdaUpdateWrapper<GoodsStore> wrapper = new LambdaUpdateWrapper<>();
			wrapper.in(GoodsStore::getGoodsId, goodIds).eq(GoodsStore::getStoreId, bill.getStoreId());
			goodsStoreMapper.delete(wrapper);
			goodsStoreMapper.updateList(goodsStores);

			bill.setStatus(-1);
			baseMapper.updateById(bill);
		}
	}


	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		LambdaUpdateWrapper<StockBill> wrapper = new LambdaUpdateWrapper<>();
		wrapper.set(StockBill::getStatus, -1).set(StockBill::getOperatorId, SecurityUtils.getUserId());
		wrapper.in(StockBill::getId, list);
		int update = baseMapper.update(null, wrapper);
		return update > 0;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public boolean save(StockBill entity) {
		entity.setType(0);
		entity.setOperatorId(SecurityUtils.getUserId());
		entity.setOperateDate(new Date());
		final boolean ret = super.save(entity);
		//存储子元素 TODO 未完成
		final List<StockBillItem> items = entity.getItemFrom();
		if (items.size() != 0) {
			items.forEach(item -> {
				item.setBillId(entity.getId());
				item.setState("2");
				final String storeId = entity.getStoreId();
				final Long amounts = Long.valueOf(item.getAmounts());
				final String goodsId = item.getGoodsId();
				//数量
				final Long mounts = goodsStoreMapper.selectByGoodsIdStoreId(goodsId, storeId);
				if (Objects.isNull(mounts)) {
					goodsStoreMapper.insert(
							Builder.builder(GoodsStore::new)
									.with(GoodsStore::setStoreId, storeId)
									.with(GoodsStore::setAmounts, amounts)
									.with(GoodsStore::setGoodsId, goodsId)
									.builder());
				}else {
					goodsStoreMapper.updateByGoodsIdStoreId(
							Builder.builder(GoodsStore::new)
									.with(GoodsStore::setStoreId, storeId)
									.with(GoodsStore::setAmounts, amounts+mounts)
									.with(GoodsStore::setGoodsId, goodsId)
									.builder()
					);
				}
				stockBillItemService.save(item);
			});
		}
		return ret;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(StockBill entity) {
		entity.setType(1);
		entity.setOperatorId(SecurityUtils.getUserId());
		entity.setOperateDate(new Date());
		final boolean ret = super.save(entity);
		//存储子元素 TODO 未完成
		final List<StockBillItem> items = entity.getItemFrom();
		if (items.size() != 0) {
			items.forEach(item -> {
				item.setBillId(entity.getId());
				item.setState("2");
				final String storeId = entity.getStoreId();
				final Long amounts = Long.valueOf(item.getAmounts());
				final String goodsId = item.getGoodsId();
				//数量
				final Long mounts = goodsStoreMapper.selectByGoodsIdStoreId(goodsId, storeId);
				if (Objects.isNull(mounts)) {
					goodsStoreMapper.insert(
							Builder.builder(GoodsStore::new)
									.with(GoodsStore::setStoreId, storeId)
									.with(GoodsStore::setAmounts, amounts)
									.with(GoodsStore::setGoodsId, goodsId)
									.builder());
				}else {
					if(mounts-amounts<0){
						throw new ServiceException("库存不够");
					}
					goodsStoreMapper.updateByGoodsIdStoreId(
							Builder.builder(GoodsStore::new)
									.with(GoodsStore::setStoreId, storeId)
									.with(GoodsStore::setAmounts, mounts-amounts)
									.with(GoodsStore::setGoodsId, goodsId)
									.builder()
					);
				}
				stockBillItemService.save(item);
			});
		}
		return ret;
	}


}
