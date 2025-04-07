import request from '@/utils/request'

/**
 * 获取桌位列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getTableList(query) {
  return request({
    url: '/admin/table/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取桌位详情
 * @param {number} id 桌位ID
 * @returns {Promise}
 */
export function getTableDetail(id) {
  return request({
    url: `/admin/table/detail/${id}`,
    method: 'get'
  })
}

/**
 * 新增桌位
 * @param {Object} data 桌位数据
 * @returns {Promise}
 */
export function addTable(data) {
  return request({
    url: '/admin/table/add',
    method: 'post',
    data
  })
}

/**
 * 修改桌位
 * @param {Object} data 桌位数据
 * @returns {Promise}
 */
export function updateTable(data) {
  return request({
    url: '/admin/table/update',
    method: 'post',
    data
  })
}

/**
 * 删除桌位
 * @param {Object} data 包含id的对象
 * @returns {Promise}
 */
export function deleteTable(data) {
  return request({
    url: '/admin/table/delete',
    method: 'post',
    data
  })
}

/**
 * 生成桌位二维码
 * @param {Object} data 包含id的对象
 * @returns {Promise}
 */
export function generateQrCode(data) {
  return request({
    url: '/admin/table/qrcode',
    method: 'post',
    data
  })
} 