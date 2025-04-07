import { request } from '../../utils/request.js';

Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null,
    isLogin: false,
    menuList: [
      {
        id: 1,
        text: '我的订单',
        url: '/pages/order/list/list',
        iconType: 'success'
      },
      {
        id: 2,
        text: '收货地址',
        url: '/pages/address/list/list',
        iconType: 'location'
      },
      {
        id: 3,
        text: '关于我们',
        url: '/pages/about/about',
        iconType: 'info'
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserInfo();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getUserInfo();
  },

  /**
   * 获取用户信息
   */
  getUserInfo: function () {
    const token = wx.getStorageSync('token');
    if (token) {
      request({
        url: '/api/mini/user/info',
        method: 'GET',
        success: (res) => {
          if (res.code === 1 && res.data) {
            this.setData({
              userInfo: res.data,
              isLogin: true
            });
          } else {
            this.setData({
              userInfo: null,
              isLogin: false
            });
          }
        },
        fail: () => {
          this.setData({
            userInfo: null,
            isLogin: false
          });
        }
      });
    } else {
      this.setData({
        userInfo: null,
        isLogin: false
      });
    }
  },

  /**
   * 用户登录
   */
  login: function () {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  /**
   * 跳转到我的订单页面
   */
  goToOrderList: function(e) {
    wx.navigateTo({
      url: '/pages/order/list/list'
    });
  },

  /**
   * 跳转菜单页面
   */
  navigateToPage: function (e) {
    const url = e.currentTarget.dataset.url;
    wx.navigateTo({
      url: url
    });
  },

  /**
   * 退出登录
   */
  logout: function () {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
          this.setData({
            userInfo: null,
            isLogin: false
          });
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
      }
    });
  }
}); 