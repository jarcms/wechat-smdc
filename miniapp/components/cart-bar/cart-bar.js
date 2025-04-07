// components/cart-bar/cart-bar.js
const cartUtil = require('../../utils/cart');

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    tableId: {
      type: Number,
      value: null
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    cartList: [],
    totalQuantity: 0,
    totalAmount: 0,
    showCartPopup: false
  },

  lifetimes: {
    attached() {
      this.updateCart();
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 更新购物车数据
    updateCart() {
      const cartList = cartUtil.getCartList();
      const totalQuantity = cartUtil.getTotalQuantity();
      const totalAmount = cartUtil.getTotalAmount();
      
      this.setData({
        cartList,
        totalQuantity,
        totalAmount
      });
    },
    
    // 显示购物车弹窗
    showCart() {
      if (this.data.totalQuantity === 0) {
        return;
      }
      
      this.setData({
        showCartPopup: true
      });
    },
    
    // 关闭购物车弹窗
    closeCart() {
      this.setData({
        showCartPopup: false
      });
    },
    
    // 清空购物车
    clearCart() {
      wx.showModal({
        title: '提示',
        content: '确定清空购物车吗？',
        success: (res) => {
          if (res.confirm) {
            cartUtil.clearCart();
            this.updateCart();
            this.closeCart();
          }
        }
      });
    },
    
    // 减少商品数量
    decreaseQuantity(e) {
      const { id, specid } = e.currentTarget.dataset;
      const item = this.data.cartList.find(item => 
        item.id === id && item.specificationId === specid
      );
      
      if (item && item.quantity > 1) {
        cartUtil.updateQuantity(id, specid, item.quantity - 1);
        this.updateCart();
      } else {
        this.removeFromCart(e);
      }
    },
    
    // 增加商品数量
    increaseQuantity(e) {
      const { id, specid } = e.currentTarget.dataset;
      const item = this.data.cartList.find(item => 
        item.id === id && item.specificationId === specid
      );
      
      if (item) {
        cartUtil.updateQuantity(id, specid, item.quantity + 1);
        this.updateCart();
      }
    },
    
    // 从购物车移除商品
    removeFromCart(e) {
      const { id, specid } = e.currentTarget.dataset;
      
      wx.showModal({
        title: '提示',
        content: '确定移除该商品吗？',
        success: (res) => {
          if (res.confirm) {
            cartUtil.removeFromCart(id, specid);
            this.updateCart();
            
            // 如果购物车为空，关闭弹窗
            if (this.data.totalQuantity === 0) {
              this.closeCart();
            }
          }
        }
      });
    },
    
    // 跳转到订单确认页面
    goToOrder() {
      if (this.data.totalQuantity === 0) {
        wx.showToast({
          title: '购物车为空',
          icon: 'none'
        });
        return;
      }
      
      if (!this.properties.tableId) {
        wx.showToast({
          title: '请先扫描桌位二维码',
          icon: 'none'
        });
        return;
      }
      
      this.closeCart();
      
      wx.navigateTo({
        url: '/pages/order/confirm/confirm?tableId=' + this.properties.tableId
      });
    }
  }
}) 