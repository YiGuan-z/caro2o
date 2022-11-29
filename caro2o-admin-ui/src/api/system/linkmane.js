import request from '@/utils/request'

// 查询客户联系人列表
export function listLinkmane(query) {
  return request({
    url: '/system/linkmane/list',
    method: 'get',
    params: query
  })
}

export function listAll() {
  return request({
    url: '/system/linkmane/listAll',
    method: 'get',
  })
}


// 查询客户联系人详细
export function getLinkmane(id) {
  return request({
    url: '/system/linkmane/' + id,
    method: 'get'
  })
}

// 新增客户联系人
export function addLinkmane(data) {
  return request({
    url: '/system/linkmane',
    method: 'post',
    data: data
  })
}

// 修改客户联系人
export function updateLinkmane(data) {
  return request({
    url: '/system/linkmane',
    method: 'put',
    data: data
  })
}

// 删除客户联系人
export function delLinkmane(id) {
  return request({
    url: '/system/linkmane/' + id,
    method: 'delete'
  })
}

// 整数范围值校验
export const intRangeValidator = (min, max) => (rule, value, callback) => {
  var isInRange = (value >= min) && (value <= max);
  const reg = /^-?\d+$/;
  var isInt = reg.test(value);
  if (isInRange && isInt){
    return callback();
  }else{
    return callback(new Error(`年龄要求在${min}-${max}的正整数`));
  }
}
