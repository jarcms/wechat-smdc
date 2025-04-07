// components/dish-card/dish-card.js
const cartUtil = require('../../utils/cart');

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    dish: {
      type: Object,
      value: {}
    },
    mode: {
      type: String,
      value: 'default' // default: 默认模式, simple: 简洁模式
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    showSpecDialog: false,
    specifications: [],
    selectedSpecId: null,
    quantity: 1
  },
  
  /**
   * 组件的方法列表
   */
  methods: {
    // 点击菜品卡片
    onTapDish() {
      this.triggerEvent('click', { dish: this.data.dish });
    },
    
    // 点击添加到购物车
    onAddToCart(e) {
      e.stopPropagation(); // 阻止事件冒泡
      
      const dish = this.data.dish;
      
      // 判断是否有规格
      if (dish.specifications && dish.specifications.length > 0) {
        // 显示规格选择对话框
        this.setData({
          showSpecDialog: true,
          specifications: dish.specifications,
          selectedSpecId: dish.specifications[0].id,
          quantity: 1
        });
      } else {
        // 无规格，直接添加到购物车
        cartUtil.addToCart(dish);
        
        // 通知父组件更新购物车状态
        this.triggerEvent('cartupdate');
      }
    },
    
    // 关闭规格对话框
    closeSpecDialog() {
      this.setData({
        showSpecDialog: false
      });
    },
    
    // 选择规格
    selectSpec(e) {
      this.setData({
        selectedSpecId: e.currentTarget.dataset.id
      });
    },
    
    // 减少数量
    decreaseQuantity() {
      if (this.data.quantity > 1) {
        this.setData({
          quantity: this.data.quantity - 1
        });
      }
    },
    
    // 增加数量
    increaseQuantity() {
      this.setData({
        quantity: this.data.quantity + 1
      });
    },
    
    // 确认添加到购物车
    confirmAddToCart() {
      const dish = this.data.dish;
      const specId = this.data.selectedSpecId;
      const quantity = this.data.quantity;
      
      // 添加到购物车
      cartUtil.addToCart(dish, specId, quantity);
      
      // 关闭对话框
      this.setData({
        showSpecDialog: false
      });
      
      // 通知父组件更新购物车状态
      this.triggerEvent('cartupdate');
    }
  }
}) 