import { request } from '../../../utils/request.js';

Page({
  /**
   * 页面的初始数据
   */
  data: {
    orderId: null,
    orderDetail: null,
    loading: true,
    statusMap: {
      1: { text: '待支付', color: '#FF9800' },
      2: { text: '已支付', color: '#4CAF50' },
      3: { text: '已完成', color: '#2196F3' },
      4: { text: '已取消', color: '#9E9E9E' }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options.id) {
      this.setData({
        orderId: options.id
      });
      this.fetchOrderDetail();
    } else {
      wx.showToast({
        title: '订单ID无效',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  /**
   * 获取订单详情
   */
  fetchOrderDetail: function () {
    this.setData({ loading: true });
    
    request({
      url: `/api/mini/order/${this.data.orderId}`,
      method: 'GET',
      success: (res) => {
        if (res.code === 1 && res.data) {
          this.setData({
            orderDetail: res.data,
            loading: false
          });
        } else {
          wx.showToast({
            title: res.msg || '获取订单详情失败',
            icon: 'none'
          });
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        });
        this.setData({ loading: false });
      }
    });
  },

  /**
   * 格式化日期时间
   */
  formatDateTime: function (dateTimeStr) {
    if (!dateTimeStr) return '';
    const date = new Date(dateTimeStr);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    const second = date.getSeconds().toString().padStart(2, '0');
    
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
  },

  /**
   * 支付订单
   */
  payOrder: function () {
    wx.showLoading({ title: '发起支付' });
    
    // 调用支付接口
    request({
      url: '/api/mini/pay/wxpay',
      method: 'POST',
      data: {
        orderId: this.data.orderId
      },
      success: (res) => {
        wx.hideLoading();
        if (res.code === 1 && res.data) {
          const payData = res.data;
          
          // 调用微信支付
          wx.requestPayment({
            timeStamp: payData.timeStamp,
            nonceStr: payData.nonceStr,
            package: payData.package,
            signType: payData.signType,
            paySign: payData.paySign,
            success: () => {
              wx.showToast({
                title: '支付成功',
                icon: 'success'
              });
              // 重新获取订单详情
              setTimeout(() => {
                this.fetchOrderDetail();
              }, 1000);
            },
            fail: (err) => {
              console.error('支付失败:', err);
              wx.showToast({
                title: '支付已取消',
                icon: 'none'
              });
            }
          });
        } else {
          wx.showToast({
            title: res.msg || '支付失败，请重试',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 取消订单
   */
  cancelOrder: function () {
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '取消中...' });
          
          request({
            url: `/api/mini/order/cancel/${this.data.orderId}`,
            method: 'POST',
            success: (res) => {
              wx.hideLoading();
              if (res.code === 1) {
                wx.showToast({
                  title: '订单已取消',
                  icon: 'success'
                });
                // 重新获取订单详情
                setTimeout(() => {
                  this.fetchOrderDetail();
                }, 1000);
              } else {
                wx.showToast({
                  title: res.msg || '取消失败，请重试',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              wx.hideLoading();
              wx.showToast({
                title: '网络异常，请重试',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  },

  /**
   * 返回订单列表
   */
  goToOrderList: function () {
    wx.navigateTo({
      url: '/pages/order/list/list'
    });
  }
}); 