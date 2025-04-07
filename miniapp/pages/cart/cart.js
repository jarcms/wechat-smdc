// pages/cart/cart.js
const app = getApp();
const cartUtil = require('../../utils/cart');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    cartList: [],
    totalQuantity: 0,
    totalAmount: 0,
    tableId: null,
    isAllSelected: true,
    selectedItems: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      tableId: app.globalData.tableId
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.updateCart();
  },
  
  /**
   * 更新购物车数据
   */
  updateCart() {
    const cartList = cartUtil.getCartList();
    const totalQuantity = cartUtil.getTotalQuantity();
    const totalAmount = cartUtil.getTotalAmount();
    
    // 初始化选中状态
    const selectedItems = cartList.map((_, index) => index);
    
    this.setData({
      cartList,
      totalQuantity,
      totalAmount,
      isAllSelected: true,
      selectedItems
    });
  },
  
  /**
   * 选择/取消选择商品
   */
  toggleSelect(e) {
    const index = e.currentTarget.dataset.index;
    const selectedItems = [...this.data.selectedItems];
    
    // 判断是否已选中
    const selectedIndex = selectedItems.indexOf(index);
    
    if (selectedIndex > -1) {
      // 已选中，取消选择
      selectedItems.splice(selectedIndex, 1);
    } else {
      // 未选中，添加选择
      selectedItems.push(index);
    }
    
    // 更新选中状态和总金额
    this.setData({
      selectedItems,
      isAllSelected: selectedItems.length === this.data.cartList.length
    });
    
    this.calculateTotal();
  },
  
  /**
   * 全选/取消全选
   */
  toggleSelectAll() {
    if (this.data.isAllSelected) {
      // 取消全选
      this.setData({
        isAllSelected: false,
        selectedItems: []
      });
    } else {
      // 全选
      const selectedItems = this.data.cartList.map((_, index) => index);
      this.setData({
        isAllSelected: true,
        selectedItems
      });
    }
    
    this.calculateTotal();
  },
  
  /**
   * 计算选中商品的总数量和总金额
   */
  calculateTotal() {
    const { cartList, selectedItems } = this.data;
    
    // 计算总数量和总金额
    let totalQuantity = 0;
    let totalAmount = 0;
    
    selectedItems.forEach(index => {
      const item = cartList[index];
      totalQuantity += item.quantity;
      totalAmount += item.price * item.quantity;
    });
    
    this.setData({
      totalQuantity,
      totalAmount
    });
  },
  
  /**
   * 减少商品数量
   */
  decreaseQuantity(e) {
    const index = e.currentTarget.dataset.index;
    const item = this.data.cartList[index];
    
    if (!item) return;
    
    if (item.quantity > 1) {
      // 数量大于1，减少数量
      cartUtil.updateQuantity(item.id, item.specificationId, item.quantity - 1);
    } else {
      // 数量为1，询问是否移除
      wx.showModal({
        title: '提示',
        content: '确定要移除该商品吗？',
        success: (res) => {
          if (res.confirm) {
            // 从购物车移除
            this.removeItem(index);
          }
        }
      });
      return;
    }
    
    this.updateCart();
  },
  
  /**
   * 增加商品数量
   */
  increaseQuantity(e) {
    const index = e.currentTarget.dataset.index;
    const item = this.data.cartList[index];
    
    if (!item) return;
    
    // 增加数量
    cartUtil.updateQuantity(item.id, item.specificationId, item.quantity + 1);
    this.updateCart();
  },
  
  /**
   * 移除商品
   */
  removeItem(index) {
    const item = this.data.cartList[index];
    
    if (!item) return;
    
    // 从购物车移除
    cartUtil.removeFromCart(item.id, item.specificationId);
    
    // 从选中列表中移除
    const selectedItems = this.data.selectedItems.filter(i => i !== index).map(i => i > index ? i - 1 : i);
    
    // 更新购物车数据
    const cartList = cartUtil.getCartList();
    const isAllSelected = selectedItems.length === cartList.length;
    
    this.setData({
      cartList,
      selectedItems,
      isAllSelected
    });
    
    this.calculateTotal();
  },
  
  /**
   * 清空购物车
   */
  clearCart() {
    if (this.data.cartList.length === 0) {
      return;
    }
    
    wx.showModal({
      title: '提示',
      content: '确定清空购物车吗？',
      success: (res) => {
        if (res.confirm) {
          cartUtil.clearCart();
          this.updateCart();
        }
      }
    });
  },
  
  /**
   * 结算
   */
  checkout() {
    // 检查是否有选中的商品
    if (this.data.selectedItems.length === 0) {
      wx.showToast({
        title: '请选择商品',
        icon: 'none'
      });
      return;
    }
    
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
      url: '/pages/order/confirm/confirm?tableId=' + this.data.tableId + '&selected=' + JSON.stringify(this.data.selectedItems)
    });
  }
}) 