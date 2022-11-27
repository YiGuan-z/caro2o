import request from '@/utils/request'

// 查询物品库存列表
export function listStore(query) {
  return request({
    url: '/store/store/list',
    method: 'get',
    params: query
  })
}

// 查询物品库存详细
export function getStore(id) {
  return request({
    url: '/store/store/' + id,
    method: 'get'
  })
}

// 新增物品库存
export function addStore(data) {
  return request({
    url: '/store/store',
    method: 'post',
    data: data
  })
}

// 修改物品库存
export function updateStore(data) {
  return request({
    url: '/store/store',
    method: 'put',
    data: data
  })
}

// 删除物品库存
export function delStore(id) {
  return request({
    url: '/store/store/' + id,
    method: 'delete'
  })
}
