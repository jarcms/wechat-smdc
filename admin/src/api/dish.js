import request from '@/utils/request'

/**
 * 获取菜品列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getDishList(query) {
  return request({
    url: '/admin/dish/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取菜品详情
 * @param {number} id 菜品ID
 * @returns {Promise}
 */
export function getDishDetail(id) {
  return request({
    url: `/admin/dish/detail/${id}`,
    method: 'get'
  })
}

/**
 * 新增菜品
 * @param {Object} data 菜品数据
 * @returns {Promise}
 */
export function addDish(data) {
  return request({
    url: '/admin/dish/add',
    method: 'post',
    data
  })
}

/**
 * 修改菜品
 * @param {Object} data 菜品数据
 * @returns {Promise}
 */
export function updateDish(data) {
  return request({
    url: '/admin/dish/update',
    method: 'post',
    data
  })
}

/**
 * 删除菜品
 * @param {Object} data 包含id的对象
 * @returns {Promise}
 */
export function deleteDish(data) {
  return request({
    url: '/admin/dish/delete',
    method: 'post',
    data
  })
}

/**
 * 修改菜品状态（起售/停售）
 * @param {Object} data 包含id和status的对象
 * @returns {Promise}
 */
export function updateDishStatus(data) {
  return request({
    url: '/admin/dish/status',
    method: 'post',
    data
  })
}

/**
 * 获取热门菜品
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getHotDishes(query) {
  return request({
    url: '/admin/dish/hot',
    method: 'get',
    params: query
  })
} 