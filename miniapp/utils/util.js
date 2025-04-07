// 工具函数集合

/**
 * 格式化时间
 * @param {Date} date 日期对象
 * @param {String} format 格式字符串，如 'YYYY-MM-DD HH:mm:ss'
 * @return {String} 格式化后的时间字符串
 */
const formatTime = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) {
    return '';
  }
  
  if (typeof date === 'string') {
    date = new Date(date.replace(/-/g, '/'));
  }
  
  if (typeof date === 'number') {
    date = new Date(date);
  }

  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  return format
    .replace('YYYY', year)
    .replace('MM', month < 10 ? '0' + month : month)
    .replace('DD', day < 10 ? '0' + day : day)
    .replace('HH', hour < 10 ? '0' + hour : hour)
    .replace('mm', minute < 10 ? '0' + minute : minute)
    .replace('ss', second < 10 ? '0' + second : second);
};

/**
 * 格式化金额
 * @param {Number} amount 金额，单位元
 * @param {Number} precision 精度，默认2位小数
 * @return {String} 格式化后的金额字符串
 */
const formatAmount = (amount, precision = 2) => {
  if (amount === undefined || amount === null || isNaN(amount)) {
    return '0.00';
  }
  return parseFloat(amount).toFixed(precision);
};

/**
 * 获取当前日期 yyyy-MM-dd
 * @return {String} 当前日期
 */
const getCurrentDate = () => {
  return formatTime(new Date(), 'YYYY-MM-DD');
};

/**
 * 生成随机字符串
 * @param {Number} length 字符串长度
 * @return {String} 随机字符串
 */
const randomString = (length = 16) => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
};

/**
 * 防抖函数
 * @param {Function} fn 需要防抖的函数
 * @param {Number} delay 延迟时间，单位毫秒
 * @return {Function} 防抖后的函数
 */
const debounce = (fn, delay = 500) => {
  let timer = null;
  return function(...args) {
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
};

/**
 * 深度拷贝对象
 * @param {Object} obj 需要拷贝的对象
 * @return {Object} 拷贝后的对象
 */
const deepClone = (obj) => {
  if (obj === null || typeof obj !== 'object') {
    return obj;
  }
  
  const result = Array.isArray(obj) ? [] : {};
  
  for (let key in obj) {
    if (obj.hasOwnProperty(key)) {
      result[key] = deepClone(obj[key]);
    }
  }
  
  return result;
};

/**
 * 检查是否为空值
 * @param {Any} value 需要检查的值
 * @return {Boolean} 是否为空
 */
const isEmpty = (value) => {
  return (
    value === undefined ||
    value === null ||
    (typeof value === 'string' && value.trim() === '') ||
    (Array.isArray(value) && value.length === 0) ||
    (typeof value === 'object' && Object.keys(value).length === 0)
  );
};

/**
 * 检查手机号格式
 * @param {String} phone 手机号
 * @return {Boolean} 是否为有效手机号
 */
const isValidPhone = (phone) => {
  return /^1[3-9]\d{9}$/.test(phone);
};

/**
 * 从本地存储获取数据，支持设置默认值
 * @param {String} key 存储键
 * @param {Any} defaultValue 默认值
 * @return {Any} 存储的数据
 */
const getStorageData = (key, defaultValue = null) => {
  try {
    const value = wx.getStorageSync(key);
    return value ? value : defaultValue;
  } catch (e) {
    console.error('getStorageData error:', e);
    return defaultValue;
  }
};

/**
 * 将数据保存到本地存储
 * @param {String} key 存储键
 * @param {Any} value 存储值
 * @return {Boolean} 是否保存成功
 */
const setStorageData = (key, value) => {
  try {
    wx.setStorageSync(key, value);
    return true;
  } catch (e) {
    console.error('setStorageData error:', e);
    return false;
  }
};

/**
 * 构建查询字符串
 * @param {Object} params 参数对象
 * @return {String} 查询字符串
 */
const buildQueryString = (params) => {
  if (!params || typeof params !== 'object') {
    return '';
  }
  
  const queryParams = [];
  
  for (let key in params) {
    if (params.hasOwnProperty(key) && params[key] !== undefined && params[key] !== null) {
      queryParams.push(`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`);
    }
  }
  
  return queryParams.length ? '?' + queryParams.join('&') : '';
};

/**
 * 获取文件扩展名
 * @param {String} filename 文件名
 * @return {String} 文件扩展名
 */
const getFileExtension = (filename) => {
  if (!filename || typeof filename !== 'string') {
    return '';
  }
  return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
};

module.exports = {
  formatTime,
  formatAmount,
  getCurrentDate,
  randomString,
  debounce,
  deepClone,
  isEmpty,
  isValidPhone,
  getStorageData,
  setStorageData,
  buildQueryString,
  getFileExtension
}; 