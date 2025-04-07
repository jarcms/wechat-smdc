<template>
  <div class="statistics-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
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

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>销售趋势</span>
            <el-radio-group v-model="salesTimeRange" size="mini" style="float: right">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container">
            <div ref="salesChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>分类销量占比</span>
          </div>
          <div class="chart-container">
            <div ref="categoryChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header">
            <span>菜品销量排行</span>
            <el-select v-model="dishRankingTimeRange" size="mini" style="float: right; width: 120px;">
              <el-option label="本周" value="week"></el-option>
              <el-option label="本月" value="month"></el-option>
              <el-option label="全部" value="all"></el-option>
            </el-select>
          </div>
          <div class="chart-container">
            <div ref="dishRankingChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStatistics, getSalesTrend, getDishRanking, getCategoryRatio } from '@/api/statistics'
import * as echarts from 'echarts/core'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  BarChart,
  LineChart,
  PieChart,
  CanvasRenderer
])

export default {
  name: 'Statistics',
  data() {
    return {
      loading: false,
      salesTimeRange: 'week',
      dishRankingTimeRange: 'week',
      statItems: [
        {
          title: '今日订单量',
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
          title: '昨日销售额',
          value: 0,
          icon: 'el-icon-s-data',
          color: '#faad14'
        },
        {
          title: '总销售额',
          value: 0,
          icon: 'el-icon-s-finance',
          color: '#f5222d'
        }
      ],
      salesChart: null,
      categoryChart: null,
      dishRankingChart: null
    }
  },
  watch: {
    salesTimeRange() {
      this.initSalesChart()
    },
    dishRankingTimeRange() {
      this.initDishRankingChart()
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.salesChart) {
      this.salesChart.dispose()
    }
    if (this.categoryChart) {
      this.categoryChart.dispose()
    }
    if (this.dishRankingChart) {
      this.dishRankingChart.dispose()
    }
  },
  methods: {
    handleResize() {
      if (this.salesChart) {
        this.salesChart.resize()
      }
      if (this.categoryChart) {
        this.categoryChart.resize()
      }
      if (this.dishRankingChart) {
        this.dishRankingChart.resize()
      }
    },
    fetchData() {
      this.loading = true
      Promise.all([
        this.fetchStatistics(),
        this.initSalesChart(),
        this.initCategoryChart(),
        this.initDishRankingChart()
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
          this.statItems[2].value = '¥' + res.data.yesterdayAmount
          this.statItems[3].value = '¥' + res.data.totalAmount
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    async initSalesChart() {
      if (!this.salesChart) {
        this.salesChart = echarts.init(this.$refs.salesChart)
      }
      
      this.salesChart.showLoading()
      try {
        const res = await getSalesTrend({ timeRange: this.salesTimeRange })
        if (res.code === 1 && res.data) {
          const { dates, amounts, orderCounts } = res.data
          const option = {
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            legend: {
              data: ['销售额', '订单量']
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              data: dates,
              axisLabel: {
                rotate: 30
              }
            },
            yAxis: [
              {
                type: 'value',
                name: '销售额',
                min: 0,
                axisLabel: {
                  formatter: '¥{value}'
                }
              },
              {
                type: 'value',
                name: '订单量',
                min: 0,
                axisLabel: {
                  formatter: '{value}单'
                }
              }
            ],
            series: [
              {
                name: '销售额',
                type: 'line',
                data: amounts,
                smooth: true,
                itemStyle: {
                  color: '#1890ff'
                }
              },
              {
                name: '订单量',
                type: 'bar',
                yAxisIndex: 1,
                data: orderCounts,
                itemStyle: {
                  color: '#52c41a'
                }
              }
            ]
          }
          this.salesChart.setOption(option)
        }
      } catch (error) {
        console.error('获取销售趋势数据失败:', error)
      } finally {
        this.salesChart.hideLoading()
      }
    },
    async initCategoryChart() {
      if (!this.categoryChart) {
        this.categoryChart = echarts.init(this.$refs.categoryChart)
      }
      
      this.categoryChart.showLoading()
      try {
        const res = await getCategoryRatio()
        if (res.code === 1 && res.data) {
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              right: 10,
              top: 'center',
              data: res.data.map(item => item.name)
            },
            series: [
              {
                name: '分类销量',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2
                },
                label: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  label: {
                    show: true,
                    fontSize: '20',
                    fontWeight: 'bold'
                  }
                },
                labelLine: {
                  show: false
                },
                data: res.data.map(item => ({
                  value: item.value,
                  name: item.name
                }))
              }
            ]
          }
          this.categoryChart.setOption(option)
        }
      } catch (error) {
        console.error('获取分类销量占比数据失败:', error)
      } finally {
        this.categoryChart.hideLoading()
      }
    },
    async initDishRankingChart() {
      if (!this.dishRankingChart) {
        this.dishRankingChart = echarts.init(this.$refs.dishRankingChart)
      }
      
      this.dishRankingChart.showLoading()
      try {
        const res = await getDishRanking({ timeRange: this.dishRankingTimeRange })
        if (res.code === 1 && res.data) {
          const dishNames = res.data.map(item => item.name)
          const dishValues = res.data.map(item => item.value)
          
          const option = {
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: {
              type: 'value',
              name: '销量',
              min: 0
            },
            yAxis: {
              type: 'category',
              data: dishNames,
              axisLabel: {
                width: 100,
                overflow: 'truncate'
              }
            },
            series: [
              {
                name: '销量',
                type: 'bar',
                data: dishValues.map(value => ({
                  value,
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                      { offset: 0, color: '#83bff6' },
                      { offset: 0.5, color: '#188df0' },
                      { offset: 1, color: '#188df0' }
                    ])
                  }
                }))
              }
            ]
          }
          this.dishRankingChart.setOption(option)
        }
      } catch (error) {
        console.error('获取菜品销量排行数据失败:', error)
      } finally {
        this.dishRankingChart.hideLoading()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-container {
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

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  position: relative;
  width: 100%;
  height: 400px;

  .chart {
    width: 100%;
    height: 100%;
  }
}
</style> 