// 网络请求工具类
const app = getApp();

// 统一的请求方法
const request = (url, method, data, showLoading = true) => {
  // 完整URL
  const fullUrl = (url.indexOf('http') === 0) ? url : app.globalData.baseUrl + url;
  
  // 显示加载中
  if (showLoading) {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
  }
  
  // 返回Promise
  return new Promise((resolve, reject) => {
    wx.request({
      url: fullUrl,
      method: method,
      data: data,
      header: {
        'content-type': 'application/json'
      },
      success: (res) => {
        if (showLoading) {
          wx.hideLoading();
        }
        
        // 统一处理返回结果
        if (res.statusCode === 200) {
          // 业务层面的成功与失败处理
          if (res.data.code === 1) {
            resolve(res.data);
          } else {
            // 业务层面的错误提示
            wx.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none',
              duration: 2000
            });
            reject(res.data);
          }
        } else {
          // HTTP异常
          wx.showToast({
            title: '网络异常，请稍后再试',
            icon: 'none',
            duration: 2000
          });
          reject({ code: res.statusCode, msg: '网络异常' });
        }
      },
      fail: (err) => {
        if (showLoading) {
          wx.hideLoading();
        }
        wx.showToast({
          title: '网络异常，请稍后再试',
          icon: 'none',
          duration: 2000
        });
        reject(err);
      }
    });
  });
};

// 封装常用请求方法
module.exports = {
  // GET请求
  get: (url, data = {}, showLoading = true) => {
    return request(url, 'GET', data, showLoading);
  },
  
  // POST请求
  post: (url, data = {}, showLoading = true) => {
    return request(url, 'POST', data, showLoading);
  },
  
  // PUT请求
  put: (url, data = {}, showLoading = true) => {
    return request(url, 'PUT', data, showLoading);
  },
  
  // DELETE请求
  delete: (url, data = {}, showLoading = true) => {
    return request(url, 'DELETE', data, showLoading);
  }
}; 