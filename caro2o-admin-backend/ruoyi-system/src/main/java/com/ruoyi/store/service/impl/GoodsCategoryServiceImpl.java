package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.store.domain.GoodsCategory;
import com.ruoyi.store.domain.TreeData;
import com.ruoyi.store.mapper.GoodsCategoryMapper;
import com.ruoyi.store.service.IGoodsCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
	public List<GoodsCategory> selectGoodsCategoryList(GoodsCategory goodsCategory) {
		//获取id参数
		final String id = (String) goodsCategory.getParent().get("id");
		//如果没有id参数就代表是查询所有
		if (id == null) {
			return getBaseMapper().selectGoodsCategoryList(goodsCategory);
		} else {
			//有id参数的化就进行处理
			final GoodsCategory result = baseMapper.selectById(id);
			String path = result.getBusiPath();
			//如果是顶级节点就直接返回
			if (path.length() == 1) return getBaseMapper().selectByPath(path);
			//构建需要查找的子路径
			path = path.substring(0, path.length() - 2);
			return getBaseMapper().selectByPath(path);
		}
	}
	
	@Override
	public List<GoodsCategory> getTreeData() {
		final List<GoodsCategory> categories = baseMapper.selectAllData();
		return buildTreeData(categories);
	}
	
	public List<GoodsCategory> buildTreeData(List<GoodsCategory> data) {
		//将查询出的对象转化为一个Map集合BusiPath为key
		final Map<String, GoodsCategory> map = data.stream()
				.collect(Collectors.toMap(GoodsCategory::getBusiPath, goods -> goods));
		//将根节点作为返回值，这一步没有顺序要求
		final List<GoodsCategory> ret = data.stream()
				.filter(node -> node.getParentId() == null)
				.collect(Collectors.toList());
		//对map进行循环操作
		map.forEach((key, node) -> {
			//判断是否是根节点，不对根节点进行处理
			final int nodeLenght = key.split(":").length;
			if (nodeLenght != 1) {
				//通过path获取父节点
				final String parentKey = key.substring(0, key.length() - 2);
				final GoodsCategory goodsCategory = map.get(parentKey);
				if (Objects.nonNull(goodsCategory)) {
					//将当前对象添加到父节点对象中
					goodsCategory.getChildren().add(node);
				}
			}
		});
		//最后对对象进行返回操作
		return ret;
	}
	
	//通过id获取它的子结构
	@Override
	public GoodsCategory getById(Serializable id) {
		return baseMapper.selectById(id);
	}
	
	public GoodsCategory getTreeById(Long id) {
		//获取它与它的子集，直接从数据库中查询，再经过树结构处理
		final List<GoodsCategory> categories = baseMapper.selectByIdFormTree(id);
		//没有子集就将子集返回出去
		if (categories.size() == 0) {
			return baseMapper.selectById(id);
		}
		//如果没有子集，就直接返回
		if (categories.size() == 1) return categories.get(0);
		final GoodsCategory rootNode = categories.get(0);
		final Map<String, GoodsCategory> map = categories.stream()
				.collect(Collectors.toMap(GoodsCategory::getBusiPath, goods -> goods));
		
		categories.stream().skip(1).forEach(node -> {
			String key = node.getBusiPath().substring(0, node.getBusiPath().length() - 2);
			map.get(key).getChildren().add(node);
		});
		return rootNode;
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
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(GoodsCategory entity) {
		this.getBusiPath(entity);
		return getBaseMapper().updateForId(entity);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean save(GoodsCategory entity) {
		this.getBusiPath(entity);
		return getBaseMapper().save(entity);
	}
	
	
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
			throw new ServiceException("非法操作，没有设置分类名称");
		}
		final String busiPath = baseMapper.selectById(entity.getParentId()).getBusiPath();
		entity.setBusiPath(busiPath + ':' + '1');
	}
	
	/**
	 * 移动分类以及子分类
	 *
	 * @param record
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public boolean moveTree(GoodsCategory record) {
		//获取是否移动子分类的参数
		final String param = ((String) record.getParams().get("son"));
		final boolean son = Boolean.parseBoolean(param);
		//获取参数
		final Map<String, Object> params = record.getParams();
		//需要移动的节点
		final Long form = (Long) params.get("from");
		//移动到的目标节点
		final Long to = (Long) params.get("to");
		//查询两个节点的信息
		final GoodsCategory source = baseMapper.selectById(form);
		final GoodsCategory target = baseMapper.selectById(to);
		// form 1:2:3
		// to 1:4:[123]:[123]
		//获取两个节点的路径
		//需要移动的节点路径
		final String formPath = source.getBusiPath();
		//移动到的目标节点路径
		final String targetPath = target.getBusiPath();
		//在目标节点中查找最大的那个节点
		String pathId = baseMapper.selectLasePath(targetPath);
		//构建节点路径
		source.setBusiPath(buildPath(pathId));
		//设置父节点id
		source.setParentId(target.getId());
		baseMapper.updateById(source);
		//如果要移动子节点
		if (son) {
			moveChildCatrgory(
					baseMapper.selectChildByPath(formPath),
					baseMapper.selectLasePath(targetPath),
					source.getId()
			);
		}
		return true;
	}
	
	/**
	 * 将子分类移动到目标上
	 *
	 * @param child      子分类
	 * @param targetPath 目标
	 */
	void moveChildCatrgory(List<GoodsCategory> child, String targetPath, Long paretnId) {
		//没有子节点的时候就直接结束
		if (child.size() == 0) return;
		child.forEach(node -> {
			node.setBusiPath(buildPath(baseMapper.selectLasePath(targetPath)));
			node.setParentId(paretnId);
			baseMapper.save(node);
		});
		
		
	}
	
	String buildPath(String path) {
		final String[] split = path.split(":");
		final int pathTemp = Integer.parseInt(split[split.length - 1]);
		split[split.length - 1] = String.valueOf(pathTemp + 1);
		return String.join(":", split);
	}
}