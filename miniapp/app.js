// app.js
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    this.login()

    // 获取系统信息
    this.getSystemInfo()
  },

  // 登录方法
  login() {
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        if (res.code) {
          // 将登录凭证发送到后端进行处理
          wx.request({
            url: this.globalData.baseUrl + '/mini/user/login',
            method: 'POST',
            data: {
              code: res.code
            },
            success: (result) => {
              if (result.data.code === 1) {
                // 登录成功，保存用户信息
                this.globalData.userInfo = result.data.data
                // 存储用户ID到本地
                wx.setStorageSync('userId', result.data.data.id)
                // 触发登录成功回调
                if (this.userInfoReadyCallback) {
                  this.userInfoReadyCallback(result.data.data)
                }
              } else {
                wx.showToast({
                  title: '登录失败',
                  icon: 'none'
                })
              }
            }
          })
        } else {
          wx.showToast({
            title: '登录失败' + res.errMsg,
            icon: 'none'
          })
        }
      }
    })
  },

  // 获取系统信息
  getSystemInfo() {
    wx.getSystemInfo({
      success: e => {
        this.globalData.StatusBar = e.statusBarHeight
        this.globalData.screenHeight = e.screenHeight
        this.globalData.screenWidth = e.screenWidth
        this.globalData.windowHeight = e.windowHeight
        this.globalData.windowWidth = e.windowWidth
      }
    })
  },

  globalData: {
    userInfo: null,
    baseUrl: 'http://localhost:8080', // 后端接口地址
    StatusBar: 0,
    screenHeight: 0,
    screenWidth: 0,
    windowHeight: 0,
    windowWidth: 0,
    tableId: null, // 当前桌位ID
    cartList: [], // 购物车数据
  }
}) 