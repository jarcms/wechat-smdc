// pages/order/confirm/confirm.js
const app = getApp();
const request = require('../../../utils/request');
const cartUtil = require('../../../utils/cart');
const util = require('../../../utils/util');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    tableId: null,
    tableName: '',
    tableCode: '',
    cartList: [],
    selectedItems: [],
    totalAmount: 0,
    remark: '',
    payMethods: [
      { id: 1, name: '微信支付', icon: '/images/wxpay.png', selected: true },
      { id: 2, name: '到店支付', icon: '/images/cash.png', selected: false }
    ],
    selectedPayMethod: 1,
    isSubmitting: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const tableId = options.tableId || app.globalData.tableId;
    let selectedItems = [];
    
    // 检查是否传入了选中项
    if (options.selected) {
      try {
        selectedItems = JSON.parse(options.selected);
      } catch (e) {
        console.error('解析选中项失败', e);
      }
    }
    
    if (!tableId) {
      wx.showModal({
        title: '提示',
        content: '请先扫描桌位二维码',
        showCancel: false,
        success: (res) => {
          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      });
      return;
    }
    
    this.setData({
      tableId,
      selectedItems
    });
    
    // 获取桌位信息
    this.getTableInfo(tableId);
    
    // 更新购物车数据
    this.updateCartData();
  },
  
  /**
   * 获取桌位信息
   */
  getTableInfo(tableId) {
    request.get('/mini/table/info/' + tableId).then(res => {
      if (res.code === 1 && res.data) {
        this.setData({
          tableName: res.data.name,
          tableCode: res.data.code
        });
      }
    });
  },
  
  /**
   * 更新购物车数据
   */
  updateCartData() {
    const cartList = cartUtil.getCartList();
    const { selectedItems } = this.data;
    
    // 如果没有选中项，则默认全选
    const itemsToShow = selectedItems.length > 0 
      ? selectedItems.map(index => cartList[index]).filter(Boolean)
      : cartList;
    
    // 计算总金额
    const totalAmount = itemsToShow.reduce((total, item) => {
      return total + (item.price * item.quantity);
    }, 0);
    
    this.setData({
      cartList: itemsToShow,
      totalAmount
    });
  },
  
  /**
   * 选择支付方式
   */
  selectPayMethod(e) {
    const id = e.currentTarget.dataset.id;
    
    // 更新选中状态
    const payMethods = this.data.payMethods.map(item => {
      return {
        ...item,
        selected: item.id === id
      };
    });
    
    this.setData({
      payMethods,
      selectedPayMethod: id
    });
  },
  
  /**
   * 输入备注
   */
  onRemarkInput(e) {
    this.setData({
      remark: e.detail.value
    });
  },
  
  /**
   * 提交订单
   */
  submitOrder() {
    // 检查是否有商品
    if (this.data.cartList.length === 0) {
      wx.showToast({
        title: '购物车为空',
        icon: 'none'
      });
      return;
    }
    
    // 防止重复提交
    if (this.data.isSubmitting) {
      return;
    }
    
    this.setData({
      isSubmitting: true
    });
    
    // 构建订单数据
    const orderData = {
      tableId: this.data.tableId,
      items: this.data.cartList.map(item => ({
        dishId: item.id,
        specificationId: item.specificationId,
        quantity: item.quantity
      })),
      payMethod: this.data.selectedPayMethod,
      remark: this.data.remark
    };
    
    // 提交订单
    request.post('/mini/order/create', orderData).then(res => {
      this.setData({
        isSubmitting: false
      });
      
      if (res.code === 1 && res.data) {
        // 订单创建成功
        const orderId = res.data.id;
        const payMethod = this.data.selectedPayMethod;
        
        // 清空购物车
        cartUtil.clearCart();
        
        // 根据支付方式进行不同处理
        if (payMethod === 1) {
          // 微信支付
          this.wxPay(orderId);
        } else {
          // 到店支付，直接跳转到订单详情
          wx.showToast({
            title: '下单成功',
            icon: 'success',
            duration: 2000,
            success: () => {
              // 跳转到订单详情页
              wx.redirectTo({
                url: '/pages/order/detail/detail?id=' + orderId
              });
            }
          });
        }
      } else {
        wx.showModal({
          title: '提示',
          content: res.msg || '创建订单失败',
          showCancel: false
        });
      }
    }).catch(err => {
      this.setData({
        isSubmitting: false
      });
      
      wx.showModal({
        title: '提示',
        content: '创建订单失败，请稍后再试',
        showCancel: false
      });
    });
  },
  
  /**
   * 发起微信支付
   */
  wxPay(orderId) {
    // 获取支付参数
    request.post('/mini/order/pay', { orderId }).then(res => {
      if (res.code === 1 && res.data) {
        // 调起微信支付
        wx.requestPayment({
          ...res.data,
          success: () => {
            // 支付成功，跳转到订单详情
            wx.showToast({
              title: '支付成功',
              icon: 'success',
              duration: 2000,
              success: () => {
                wx.redirectTo({
                  url: '/pages/order/detail/detail?id=' + orderId
                });
              }
            });
          },
          fail: (err) => {
            wx.showModal({
              title: '提示',
              content: '支付失败，您可以稍后在订单列表中重新支付',
              showCancel: false,
              success: () => {
                wx.redirectTo({
                  url: '/pages/order/detail/detail?id=' + orderId
                });
              }
            });
          }
        });
      } else {
        wx.showModal({
          title: '提示',
          content: res.msg || '发起支付失败',
          showCancel: false,
          success: () => {
            wx.redirectTo({
              url: '/pages/order/detail/detail?id=' + orderId
            });
          }
        });
      }
    }).catch(() => {
      wx.showModal({
        title: '提示',
        content: '发起支付失败，请稍后在订单列表中重新支付',
        showCancel: false,
        success: () => {
          wx.redirectTo({
            url: '/pages/order/detail/detail?id=' + orderId
          });
        }
      });
    });
  }
})