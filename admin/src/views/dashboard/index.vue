<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6" v-for="(item, index) in statItems" :key="index">
        <el-card class="stat-card" :body-style="{ padding: '0px' }">
          <div class="card-content" :style="{ backgroundColor: item.color }">
            <div class="icon-container">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 最近订单 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>最近订单</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="goToOrderList">查看更多</el-button>
          </div>
          <el-table :data="recentOrders" style="width: 100%" v-loading="loading">
            <el-table-column prop="number" label="订单号" width="180"></el-table-column>
            <el-table-column prop="amount" label="金额" width="100">
              <template slot-scope="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template slot-scope="scope">
                <el-tag :type="getOrderStatusType(scope.row.status)">
                  {{ getOrderStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 热销菜品 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>热销菜品</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="goToDishList">查看更多</el-button>
          </div>
          <el-table :data="hotDishes" style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="菜品名称"></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column>
            <el-table-column prop="price" label="价格">
              <template slot-scope="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="saleCount" label="销量"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getRecentOrders } from '@/api/order'
import { getHotDishes } from '@/api/dish'
import { getStatistics } from '@/api/statistics'

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: false,
      recentOrders: [],
      hotDishes: [],
      statItems: [
        {
          title: '今日订单',
          value: 0,
          icon: 'el-icon-s-order',
          color: '#1890ff'
        },
        {
          title: '今日销售额',
          value: 0,
          icon: 'el-icon-money',
          color: '#52c41a'
        },
        {
          title: '总菜品数',
          value: 0,
          icon: 'el-icon-dish',
          color: '#faad14'
        },
        {
          title: '总用户数',
          value: 0,
          icon: 'el-icon-user',
          color: '#f5222d'
        }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      Promise.all([
        this.fetchStatistics(),
        this.fetchRecentOrders(),
        this.fetchHotDishes()
      ]).finally(() => {
        this.loading = false
      })
    },
    async fetchStatistics() {
      try {
        const res = await getStatistics()
        if (res.code === 1 && res.data) {
          this.statItems[0].value = res.data.todayOrderCount
          this.statItems[1].value = '¥' + res.data.todayAmount
          this.statItems[2].value = res.data.dishCount
          this.statItems[3].value = res.data.userCount
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    async fetchRecentOrders() {
      try {
        const res = await getRecentOrders()
        if (res.code === 1 && res.data) {
          this.recentOrders = res.data
        }
      } catch (error) {
        console.error('获取最近订单失败:', error)
      }
    },
    async fetchHotDishes() {
      try {
        const res = await getHotDishes()
        if (res.code === 1 && res.data) {
          this.hotDishes = res.data
        }
      } catch (error) {
        console.error('获取热销菜品失败:', error)
      }
    },
    getOrderStatusText(status) {
      const statusMap = {
        1: '待付款',
        2: '待接单',
        3: '待上菜',
        4: '已完成',
        5: '已取消'
      }
      return statusMap[status] || '未知状态'
    },
    getOrderStatusType(status) {
      const typeMap = {
        1: 'warning',
        2: 'primary',
        3: 'info',
        4: 'success',
        5: 'danger'
      }
      return typeMap[status] || ''
    },
    goToOrderList() {
      this.$router.push('/order/list')
    },
    goToDishList() {
      this.$router.push('/dish/list')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;

  .card-content {
    display: flex;
    align-items: center;
    height: 120px;
    padding: 20px;
    color: #fff;
  }

  .icon-container {
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.2);
    font-size: 30px;
    margin-right: 20px;
  }

  .stat-info {
    .stat-value {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stat-title {
      font-size: 14px;
      opacity: 0.8;
    }
  }
}

.box-card {
  margin-bottom: 20px;
}
</style> 