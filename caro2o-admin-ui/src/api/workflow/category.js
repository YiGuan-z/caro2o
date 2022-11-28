import request from '@/utils/request'

// 查询物品分类信息列表
export function listCategory(query) {
  return request({
    url: '/workflow/category/list',
    method: 'get',
    params: query
  })
}

// 查询物品分类信息详细
export function getCategory(id) {
  return request({
    url: '/workflow/category/' + id,
    method: 'get'
  })
}

// 新增物品分类信息
export function addCategory(data) {
  return request({
    url: '/workflow/category',
    method: 'post',
    data: data
  })
}

// 修改物品分类信息
export function updateCategory(data) {
  return request({
    url: '/workflow/category',
    method: 'put',
    data: data
  })
}

// 删除物品分类信息
export function delCategory(id) {
  return request({
    url: '/workflow/category/' + id,
    method: 'delete'
  })
}

// 查询部门下拉树结构
export function getTreeDate() {
  return request({
    url: '/workflow/category/treeList',
    method: 'get'
  })
}