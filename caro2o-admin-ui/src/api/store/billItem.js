import request from '@/utils/request'

// 查询出入库单据列表
export function listBillItem() {
  return request({
    url: '/store/billItem/list',
    method: 'get',
  })
}

// 查询出入库单据详细
export function getBillItem(id) {
  return request({
    url: '/store/billItem/' + id,
    method: 'get'
  })
}

// 新增出入库单据
export function addBillItem(data) {
  return request({
    url: '/store/billItem',
    method: 'post',
    data: data
  })
}

// 修改出入库单据
export function updateBillItem(data) {
  return request({
    url: '/store/billItem',
    method: 'put',
    data: data
  })
}

// 删除出入库单据
export function delBillItem(id) {
  return request({
    url: '/store/billItem/' + id,
    method: 'delete'
  })
}
