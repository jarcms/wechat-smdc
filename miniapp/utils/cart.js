// 购物车工具类
const app = getApp();

// 添加到购物车
const addToCart = (dish, specificationId = null, quantity = 1) => {
  const cartList = app.globalData.cartList || [];
  
  // 查找购物车中是否已存在该菜品（同一规格）
  const index = cartList.findIndex(item => {
    return item.id === dish.id && item.specificationId === specificationId;
  });
  
  if (index > -1) {
    // 已存在，数量+1
    cartList[index].quantity += quantity;
  } else {
    // 不存在，添加新条目
    cartList.push({
      id: dish.id,
      name: dish.name,
      price: dish.price,
      image: dish.image,
      specificationId: specificationId,
      specificationName: specificationId ? getSpecificationName(specificationId, dish) : null,
      quantity: quantity
    });
  }
  
  // 更新全局购物车数据
  app.globalData.cartList = cartList;
  
  // 触发购物车更新事件
  wx.showToast({
    title: '已加入购物车',
    icon: 'success'
  });
  
  return cartList;
};

// 从购物车移除
const removeFromCart = (dishId, specificationId = null) => {
  let cartList = app.globalData.cartList || [];
  
  // 查找购物车中的菜品
  const index = cartList.findIndex(item => {
    return item.id === dishId && item.specificationId === specificationId;
  });
  
  if (index > -1) {
    // 从购物车中移除
    cartList.splice(index, 1);
    
    // 更新全局购物车数据
    app.globalData.cartList = cartList;
  }
  
  return cartList;
};

// 更新购物车中菜品数量
const updateQuantity = (dishId, specificationId = null, quantity = 1) => {
  let cartList = app.globalData.cartList || [];
  
  // 查找购物车中的菜品
  const index = cartList.findIndex(item => {
    return item.id === dishId && item.specificationId === specificationId;
  });
  
  if (index > -1) {
    if (quantity <= 0) {
      // 数量为0，从购物车移除
      cartList.splice(index, 1);
    } else {
      // 更新数量
      cartList[index].quantity = quantity;
    }
    
    // 更新全局购物车数据
    app.globalData.cartList = cartList;
  }
  
  return cartList;
};

// 清空购物车
const clearCart = () => {
  app.globalData.cartList = [];
  return [];
};

// 获取购物车数据
const getCartList = () => {
  return app.globalData.cartList || [];
};

// 获取购物车中总数量
const getTotalQuantity = () => {
  const cartList = app.globalData.cartList || [];
  return cartList.reduce((total, item) => total + item.quantity, 0);
};

// 获取购物车中总金额
const getTotalAmount = () => {
  const cartList = app.globalData.cartList || [];
  return cartList.reduce((total, item) => total + item.price * item.quantity, 0);
};

// 获取规格名称
const getSpecificationName = (specificationId, dish) => {
  if (!dish.specifications || !Array.isArray(dish.specifications)) {
    return '';
  }
  
  const specification = dish.specifications.find(spec => spec.id === specificationId);
  return specification ? specification.name : '';
};

module.exports = {
  addToCart,
  removeFromCart,
  updateQuantity,
  clearCart,
  getCartList,
  getTotalQuantity,
  getTotalAmount
}; 