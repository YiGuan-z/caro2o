import request from '@/utils/request'

// 查询拜访信息列表
export function listVisit(query) {
  return request({
    url: '/customer/visit/list',
    method: 'get',
    params: query
  })
}

// 查询拜访信息详细
export function getVisit(id) {
  return request({
    url: '/customer/visit/' + id,
    method: 'get'
  })
}

// 新增拜访信息
export function addVisit(data) {
  return request({
    url: '/customer/visit',
    method: 'post',
    data: data
  })
}

// 修改拜访信息
export function updateVisit(data) {
  return request({
    url: '/customer/visit',
    method: 'put',
    data: data
  })
}

// 删除拜访信息
export function delVisit(id) {
  return request({
    url: '/customer/visit/' + id,
    method: 'delete'
  })
}
