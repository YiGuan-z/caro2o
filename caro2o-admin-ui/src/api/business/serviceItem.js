import request from '@/utils/request'


// 查询参数列表
export function listServiceItem(query) {
  return request({
    url: '/business/serviceItem',
    method: 'get',
    params: query
  })
}

// add养修服务单项
export function addServiceItem(params) {
  return request({
    url: '/business/serviceItem',
    method: 'post',
    data: params
  })
}

// 编辑更新养修服务单项
export function updateServiceItem(params) {
  return request({
    url: '/business/serviceItem',
    method: 'put',
    data: params
  })
}
//获取养修服务单项详情
export function getServiceItem(params) {
  return request({
    url: '/business/serviceItem/' + params.id,
    method: 'get',
  })
}
//养修服务单项上架
export function serviceItemSaleOn(params) {
  return request({
    url: '/business/serviceItem/saleOn/' + params.id,
    method: 'put'
  })
}
//养修服务单项下架
export function serviceItemSaleOff(params) {
  return request({
    url: '/business/serviceItem/saleOff/' + params.id,
    method: 'put'
  })
}
