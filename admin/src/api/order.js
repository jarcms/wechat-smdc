import request from '@/utils/request'

/**
 * 获取订单列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getOrderList(query) {
  return request({
    url: '/admin/order/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取订单详情
 * @param {Number} id 订单ID
 * @returns {Promise}
 */
export function getOrderDetail(id) {
  return request({
    url: `/admin/order/${id}`,
    method: 'get'
  })
}

/**
 * 接单
 * @param {Number} id 订单ID
 * @returns {Promise}
 */
export function acceptOrder(id) {
  return request({
    url: `/admin/order/accept/${id}`,
    method: 'post'
  })
}

/**
 * 完成订单
 * @param {Number} id 订单ID
 * @returns {Promise}
 */
export function completeOrder(id) {
  return request({
    url: `/admin/order/complete/${id}`,
    method: 'post'
  })
}

/**
 * 取消订单
 * @param {Number} id 订单ID
 * @param {String} reason 取消原因
 * @returns {Promise}
 */
export function cancelOrder(id, reason) {
  return request({
    url: `/admin/order/cancel/${id}`,
    method: 'post',
    data: {
      reason
    }
  })
}

/**
 * 获取订单统计数据
 * @returns {Promise}
 */
export function getOrderStatistics() {
  return request({
    url: '/admin/order/statistics',
    method: 'get'
  })
}

/**
 * 获取最近订单
 * @param {Number} limit 获取条数
 * @returns {Promise}
 */
export function getRecentOrders(limit = 5) {
  return request({
    url: '/admin/order/recent',
    method: 'get',
    params: { limit }
  })
}