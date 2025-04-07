import { request } from '../../../utils/request.js';

Page({
  /**
   * 页面的初始数据
   */
  data: {
    orderList: [],
    loading: true,
    loadingMore: false,
    page: 1,
    pageSize: 10,
    hasMore: true,
    statusMap: {
      1: { text: '待支付', color: '#FF9800' },
      2: { text: '已支付', color: '#4CAF50' },
      3: { text: '已完成', color: '#2196F3' },
      4: { text: '已取消', color: '#9E9E9E' }
    },
    tabs: [
      { id: 0, name: '全部' },
      { id: 1, name: '待支付' },
      { id: 2, name: '已支付' },
      { id: 3, name: '已完成' },
      { id: 4, name: '已取消' }
    ],
    activeTab: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.fetchOrderList();
  },
  
  /**
   * 切换标签
   */
  switchTab: function (e) {
    const tabId = e.currentTarget.dataset.id;
    if (tabId !== this.data.activeTab) {
      this.setData({
        activeTab: tabId,
        orderList: [],
        page: 1,
        hasMore: true
      });
      this.fetchOrderList();
    }
  },

  /**
   * 获取订单列表
   */
  fetchOrderList: function () {
    if (!this.data.hasMore || this.data.loadingMore) return;
    
    const isFirstPage = this.data.page === 1;
    this.setData({
      loading: isFirstPage,
      loadingMore: !isFirstPage
    });

    const params = {
      page: this.data.page,
      pageSize: this.data.pageSize
    };
    
    // 如果不是"全部"标签，则添加状态过滤
    if (this.data.activeTab > 0) {
      params.status = this.data.activeTab;
    }
    
    request({
      url: '/api/mini/order/list',
      method: 'GET',
      data: params,
      success: (res) => {
        if (res.code === 1 && res.data) {
          const newList = [...this.data.orderList, ...res.data.list];
          const hasMore = newList.length < res.data.total;
          
          this.setData({
            orderList: newList,
            hasMore: hasMore,
            page: hasMore ? this.data.page + 1 : this.data.page,
            loading: false,
            loadingMore: false
          });
        } else {
          wx.showToast({
            title: res.msg || '获取订单列表失败',
            icon: 'none'
          });
          this.setData({
            loading: false,
            loadingMore: false
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        });
        this.setData({
          loading: false,
          loadingMore: false
        });
      }
    });
  },

  /**
   * 格式化日期
   */
  formatDate: function (dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  /**
   * 查看订单详情
   */
  viewOrderDetail: function (e) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order/detail/detail?id=${orderId}`
    });
  },

  /**
   * 支付订单
   */
  payOrder: function (e) {
    const orderId = e.currentTarget.dataset.id;
    wx.showLoading({ title: '发起支付' });
    
    request({
      url: '/api/mini/pay/wxpay',
      method: 'POST',
      data: {
        orderId: orderId
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
              // 重新获取订单列表
              setTimeout(() => {
                this.setData({
                  orderList: [],
                  page: 1,
                  hasMore: true
                });
                this.fetchOrderList();
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
    return false;
  },

  /**
   * 取消订单
   */
  cancelOrder: function (e) {
    const orderId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '取消中...' });
          
          request({
            url: `/api/mini/order/cancel/${orderId}`,
            method: 'POST',
            success: (res) => {
              wx.hideLoading();
              if (res.code === 1) {
                wx.showToast({
                  title: '订单已取消',
                  icon: 'success'
                });
                // 重新获取订单列表
                setTimeout(() => {
                  this.setData({
                    orderList: [],
                    page: 1,
                    hasMore: true
                  });
                  this.fetchOrderList();
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
    return false;
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    this.setData({
      orderList: [],
      page: 1,
      hasMore: true
    });
    this.fetchOrderList();
    setTimeout(() => {
      wx.stopPullDownRefresh();
    }, 1000);
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    if (this.data.hasMore) {
      this.fetchOrderList();
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {},

  /**
   * 去点餐
   */
  goToIndex: function () {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
}); 