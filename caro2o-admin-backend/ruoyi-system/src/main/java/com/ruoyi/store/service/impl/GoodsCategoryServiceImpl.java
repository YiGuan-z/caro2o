package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.store.domain.GoodsCategory;
import com.ruoyi.store.domain.TreeData;
import com.ruoyi.store.mapper.GoodsCategoryMapper;
import com.ruoyi.store.service.IGoodsCategoryService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 物品分类信息Service业务层处理
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements IGoodsCategoryService {
	/**
	 * 查询物品分类信息列表
	 *
	 * @param goodsCategory 物品分类信息
	 * @return 物品分类信息
	 */
	@Override
	public List<GoodsCategory> selectGoodsCategoryList(GoodsCategory goodsCategory) {
		return getBaseMapper().selectGoodsCategoryList(goodsCategory);
	}
	

	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		for (Object o : list) {
			Long id = (Long) o;
			GoodsCategory goodsCategory = getBaseMapper().selectById(id);
			this.removeRecursion(goodsCategory);
		}
		return super.removeBatchByIds(list);
	}

	private void removeRecursion(GoodsCategory goodsCategory) {
		List<GoodsCategory> children = getBaseMapper().selectChild(goodsCategory.getId());
		if (children == null || children.size() == 0) {
			return;
		}
		GoodsCategory child = null;
		for (GoodsCategory category : children) {
			child = category;
			getBaseMapper().deleteForId(category.getId());
			// 递归
			this.removeRecursion(child);
		}

	}

	@Override
	public boolean updateById(GoodsCategory entity) {
		this.getBusiPath(entity);
		return getBaseMapper().updateForId(entity);
	}

	@Override
	public boolean save(GoodsCategory entity) {
		this.getBusiPath(entity);
		return getBaseMapper().save(entity);
	}

	@Override
	public GoodsCategory getById(Serializable id) {
		return getBaseMapper().selectById(id);
	}

	@Override
	public List<TreeData> queryTreeList() {
		// 1. 创建 TreeData 集合
		ArrayList<TreeData> treeList = new ArrayList<>();
		// 2. 获取所有根元素
		// 狼码教育
		List<GoodsCategory> roots = getBaseMapper().selectChildrenByParentId(null);

		TreeData treeData = null;
		for (GoodsCategory root : roots) {
			// 将根节点的部门对象转换为 树对象
			treeData = new TreeData(root.getId(), root.getCategoryName());
			// 并将其存入最终的集合中
			treeList.add(treeData);

			// 将当前树对象作为根节点, 并且当前节点的 id 作为初始 id 用于查询是否有子元素
			this.treeListRecursion(treeData, root.getId());
		}
		// [狼码教育]
		return treeList;
	}

	private void treeListRecursion(TreeData treeData, Long parentId) {
		// 先基于当前根元素 id 来查询子元素
		// 1 轮: 查狼码教育下所有的子部门
		// 2 轮: 经过总经办/产品部/销售部各自进行递归查询
		//  总经办与产品部没有子节点
		//  销售部有子节点, 因此销售部继续往下执行
		List<GoodsCategory> departments = getBaseMapper().selectChildrenByParentId(parentId);
		// 递归出口, 如果当前部门没有子部门了, 就不再递归了
		if (departments == null || departments.size() == 0) {
			return;
		}

		// 将当前元素的子元素作为递归数据
		TreeData child = null;
		// 遍历当前查到的子部门集合, 并将每个对象转换为树对象
		for (GoodsCategory department : departments) {
			// 将每个部门对象转换为树对象
			child = new TreeData(department.getId(), department.getCategoryName());
			// 将当前树对象加入到当前这棵树的根节点中
			treeData.getChildren().add(child);

			// 递归
			this.treeListRecursion(child, child.getId());
		}
	}

	private void getBusiPath(GoodsCategory entity) {
		if (entity.getCategoryName() == null) {
			throw new ServiceException("非法操作");
		}
		String busiPath = entity.getCategoryName().substring(2).replace("-", ":");
		entity.setBusiPath(busiPath);
	}
}