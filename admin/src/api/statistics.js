import request from '@/utils/request'

/**
 * 获取统计数据
 * @returns {Promise}
 */
export function getStatistics() {
  return request({
    url: '/admin/statistics/data',
    method: 'get'
  })
}

/**
 * 获取销售趋势数据
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getSalesTrend(query) {
  return request({
    url: '/admin/statistics/sales',
    method: 'get',
    params: query
  })
}

/**
 * 获取菜品销量排行数据
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getDishRanking(query) {
  return request({
    url: '/admin/statistics/dish/ranking',
    method: 'get',
    params: query
  })
}

/**
 * 获取分类销量占比
 * @returns {Promise}
 */
export function getCategoryRatio() {
  return request({
    url: '/admin/statistics/category/ratio',
    method: 'get'
  })
} 