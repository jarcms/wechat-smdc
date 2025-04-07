// pages/dish/dish.js
const app = getApp();
const request = require('../../utils/request');
const cartUtil = require('../../utils/cart');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    dishId: null,
    dish: null,
    currentSpecification: null,
    quantity: 1,
    isLoading: false,
    tableId: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const dishId = options.id;
    
    if (!dishId) {
      wx.showToast({
        title: '菜品ID不能为空',
        icon: 'none'
      });
      return;
    }
    
    this.setData({
      dishId: dishId,
      tableId: app.globalData.tableId
    });
    
    // 加载菜品详情
    this.loadDishDetail();
  },

  /**
   * 加载菜品详情
   */
  loadDishDetail() {
    this.setData({
      isLoading: true
    });
    
    request.get('/mini/dish/detail/' + this.data.dishId).then(res => {
      if (res.code === 1 && res.data) {
        // 设置菜品数据
        const dish = res.data;
        const currentSpecification = dish.specifications && dish.specifications.length > 0 
          ? dish.specifications[0] 
          : null;
          
        this.setData({
          dish: dish,
          currentSpecification: currentSpecification,
          isLoading: false
        });
      } else {
        this.setData({
          isLoading: false
        });
        
        wx.showToast({
          title: '获取菜品详情失败',
          icon: 'none'
        });
      }
    }).catch(() => {
      this.setData({
        isLoading: false
      });
      
      wx.showToast({
        title: '获取菜品详情失败',
        icon: 'none'
      });
    });
  },
  
  /**
   * 选择规格
   */
  selectSpecification(e) {
    const specId = e.currentTarget.dataset.id;
    const specification = this.data.dish.specifications.find(item => item.id === specId);
    
    if (specification) {
      this.setData({
        currentSpecification: specification
      });
    }
  },
  
  /**
   * 减少数量
   */
  decreaseQuantity() {
    if (this.data.quantity > 1) {
      this.setData({
        quantity: this.data.quantity - 1
      });
    }
  },
  
  /**
   * 增加数量
   */
  increaseQuantity() {
    this.setData({
      quantity: this.data.quantity + 1
    });
  },
  
  /**
   * 添加到购物车
   */
  addToCart() {
    const dish = this.data.dish;
    const specId = this.data.currentSpecification ? this.data.currentSpecification.id : null;
    const quantity = this.data.quantity;
    
    // 添加到购物车
    cartUtil.addToCart(dish, specId, quantity);
  },
  
  /**
   * 立即下单
   */
  orderNow() {
    // 先添加到购物车
    this.addToCart();
    
    // 检查是否已选择桌位
    if (!this.data.tableId) {
      wx.showModal({
        title: '提示',
        content: '请先扫描桌位二维码',
        confirmText: '去扫码',
        success: (res) => {
          if (res.confirm) {
            wx.switchTab({
              url: '/pages/index/index'
            });
          }
        }
      });
      return;
    }
    
    // 跳转到订单确认页面
    wx.navigateTo({
      url: '/pages/order/confirm/confirm?tableId=' + this.data.tableId
    });
  },
  
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    this.loadDishDetail();
    wx.stopPullDownRefresh();
  },
  
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    return {
      title: this.data.dish ? this.data.dish.name : '菜品详情',
      path: '/pages/dish/dish?id=' + this.data.dishId
    };
  }
}) 