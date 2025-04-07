import request from '@/utils/request'

/**
 * 获取分类列表
 * @returns {Promise}
 */
export function getCategoryList() {
  return request({
    url: '/admin/category/list',
    method: 'get'
  })
}

/**
 * 获取分类（分页）
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getCategoryPage(query) {
  return request({
    url: '/admin/category/page',
    method: 'get',
    params: query
  })
}

/**
 * 添加分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function addCategory(data) {
  return request({
    url: '/admin/category/add',
    method: 'post',
    data
  })
}

/**
 * 更新分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function updateCategory(data) {
  return request({
    url: '/admin/category/update',
    method: 'post',
    data
  })
}

/**
 * 删除分类
 * @param {Object} data 包含id的对象
 * @returns {Promise}
 */
export function deleteCategory(data) {
  return request({
    url: '/admin/category/delete',
    method: 'post',
    data
  })
} 