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

// 审核通过合同项信息
export function auditPass(id) {
  return request({
    url: '/contract/itemInfo/pass/' + id,
    method: 'patch'
  })
}
// 审核拒绝合同项信息
export function auditReject(id) {
  return request({
    url: '/contract/itemInfo/reject/' + id,
    method: 'patch'
  })
}
// 确认盖章
export function auditConfirm(id) {
  return request({
    url: '/contract/itemInfo/confirm/' + id,
    method: 'patch'
  })
}
// 作废合同
export function failure(id) {
  return request({
    url: '/contract/itemInfo/failure/' + id,
    method: 'patch'
  })
}
