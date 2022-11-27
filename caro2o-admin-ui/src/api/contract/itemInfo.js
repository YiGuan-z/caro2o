import request from '@/utils/request'

// 查询合同项信息列表
export function listItemInfo(query) {
  return request({
    url: '/contract/itemInfo/list',
    method: 'get',
    params: query
  })
}

// 查询合同项信息详细
export function getItemInfo(id) {
  return request({
    url: '/contract/itemInfo/' + id,
    method: 'get'
  })
}

// 新增合同项信息
export function addItemInfo(data) {
  return request({
    url: '/contract/itemInfo',
    method: 'post',
    data: data
  })
}

// 修改合同项信息
export function updateItemInfo(data) {
  return request({
    url: '/contract/itemInfo',
    method: 'put',
    data: data
  })
}

// 删除合同项信息
export function delItemInfo(id) {
  return request({
    url: '/contract/itemInfo/' + id,
    method: 'delete'
  })
}
