const app = getApp();
const request = require('../../utils/request');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    tableId: null,
    tableName: '',
    tableCode: '',
    isTableScanned: false,
    userInfo: null,
    banners: [] // 轮播图数据
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 检查是否扫描了桌位二维码
    if (options.q) {
      // 解析二维码中的桌位ID
      const q = decodeURIComponent(options.q);
      // 假设q的格式是：https://xxx.com?tableId=123&tableCode=A001
      const tableId = this.getParamFromUrl(q, 'tableId');
      const tableCode = this.getParamFromUrl(q, 'tableCode');
      
      if (tableId) {
        this.setData({
          tableId: tableId,
          tableCode: tableCode,
          isTableScanned: true
        });
        
        // 保存桌位信息到全局数据
        app.globalData.tableId = tableId;
        
        // 获取桌位详情
        this.getTableInfo(tableId);
      }
    }
    
    // 获取用户信息
    this.getUserInfo();
    
    // 获取轮播图数据
    this.getBanners();
  },
  
  /**
   * 从URL中获取参数值
   */
  getParamFromUrl(url, param) {
    const reg = new RegExp('[?&]' + param + '=([^&#]*)', 'i');
    const match = url.match(reg);
    return match ? match[1] : null;
  },
  
  /**
   * 获取桌位信息
   */
  getTableInfo(tableId) {
    request.get('/mini/table/info/' + tableId).then(res => {
      if (res.code === 1 && res.data) {
        this.setData({
          tableName: res.data.name
        });
      }
    });
  },
  
  /**
   * 获取用户信息
   */
  getUserInfo() {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo
      });
    } else {
      // 等待登录完成
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res
        });
      };
    }
  },
  
  /**
   * 获取轮播图数据
   */
  getBanners() {
    // 模拟数据，实际项目中应该从服务器获取
    this.setData({
      banners: [
        {
          id: 1,
          imageUrl: '/images/banner1.jpg',
          linkUrl: '/pages/category/category'
        },
        {
          id: 2,
          imageUrl: '/images/banner2.jpg',
          linkUrl: '/pages/dish/dish?id=2'
        },
        {
          id: 3,
          imageUrl: '/images/banner3.jpg',
          linkUrl: '/pages/user/user'
        }
      ]
    });
  },
  
  /**
   * 跳转到点餐页面
   */
  goToOrder() {
    if (!this.data.isTableScanned) {
      wx.showToast({
        title: '请先扫描桌位二维码',
        icon: 'none'
      });
      return;
    }
    
    wx.switchTab({
      url: '/pages/category/category'
    });
  },
  
  /**
   * 打开扫码界面
   */
  scanTable() {
    wx.scanCode({
      onlyFromCamera: true,
      success: (res) => {
        // 解析扫码结果，获取桌位ID
        const q = decodeURIComponent(res.result);
        const tableId = this.getParamFromUrl(q, 'tableId');
        const tableCode = this.getParamFromUrl(q, 'tableCode');
        
        if (tableId) {
          this.setData({
            tableId: tableId,
            tableCode: tableCode,
            isTableScanned: true
          });
          
          // 保存桌位信息到全局数据
          app.globalData.tableId = tableId;
          
          // 获取桌位详情
          this.getTableInfo(tableId);
        } else {
          wx.showToast({
            title: '无效的桌位二维码',
            icon: 'none'
          });
        }
      }
    });
  },
  
  /**
   * 点击轮播图
   */
  onBannerClick(e) {
    const index = e.currentTarget.dataset.index;
    const banner = this.data.banners[index];
    
    if (banner && banner.linkUrl) {
      wx.navigateTo({
        url: banner.linkUrl
      });
    }
  },
  
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    return {
      title: '扫码点餐',
      path: '/pages/index/index'
    };
  }
}) 