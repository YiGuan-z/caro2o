import request from '@/utils/request'

// 查询物品信息列表
export function listGoods(query) {
  return request({
    url: '/store/goods/list',
    method: 'get',
    params: query
  })
}

// 查询物品信息详细
export function getGoods(id) {
  return request({
    url: '/store/goods/' + id,
    method: 'get'
  })
}

// 新增物品信息
export function addGoods(data) {
  return request({
    url: '/store/goods',
    method: 'post',
    data: data
  })
}

// 修改物品信息
export function updateGoods(data) {
  return request({
    url: '/store/goods',
    method: 'put',
    data: data
  })
}

// 删除物品信息
export function delGoods(id) {
  return request({
    url: '/store/goods/' + id,
    method: 'delete'
  })
}
