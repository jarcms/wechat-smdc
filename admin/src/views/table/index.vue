<template>
  <div class="table-container">
    <!-- 操作栏 -->
    <div class="action-container">
      <el-button type="primary" @click="handleAdd">新增桌位</el-button>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="加载中..."
      border
      fit
      highlight-current-row
    >
      <el-table-column label="ID" width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="桌位名称" min-width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="容纳人数" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.capacity }}人</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? '空闲' : '使用中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="二维码" width="120" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="info" @click="showQrCode(scope.row)">查看二维码</el-button>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="listQuery.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 新增/编辑桌位对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增桌位' : '编辑桌位'" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="桌位名称" prop="name">
          <el-input v-model="temp.name" placeholder="请输入桌位名称"></el-input>
        </el-form-item>
        <el-form-item label="容纳人数" prop="capacity">
          <el-input-number v-model="temp.capacity" :min="1" :max="20"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 二维码对话框 -->
    <el-dialog title="桌位二维码" :visible.sync="qrDialogVisible" width="30%">
      <div class="qrcode-container">
        <div class="qrcode-image">
          <img v-if="currentQrCode" :src="currentQrCode" alt="桌位二维码">
          <div v-else class="no-qrcode">二维码不存在，请先生成</div>
        </div>
        <div class="qrcode-info">
          <p>桌位: {{ currentTable.name }}</p>
          <p>容纳人数: {{ currentTable.capacity }}人</p>
        </div>
        <div class="qrcode-actions">
          <el-button type="primary" @click="generateQrCode" :loading="qrLoading">{{ currentQrCode ? '重新生成' : '生成二维码' }}</el-button>
          <el-button type="success" @click="downloadQrCode" :disabled="!currentQrCode">下载二维码</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTableList, addTable, updateTable, deleteTable, generateQrCode } from '@/api/table'

export default {
  name: 'TableManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        pageSize: 10
      },
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        name: '',
        capacity: 4
      },
      rules: {
        name: [{ required: true, message: '请输入桌位名称', trigger: 'blur' }],
        capacity: [{ required: true, message: '请输入容纳人数', trigger: 'blur' }]
      },
      qrDialogVisible: false,
      currentTable: {},
      currentQrCode: '',
      qrLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const res = await getTableList(this.listQuery)
        if (res.code === 1) {
          this.list = res.data.records || []
          this.total = res.data.total || 0
        }
      } catch (error) {
        console.error('获取桌位列表失败:', error)
      } finally {
        this.listLoading = false
      }
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        capacity: 4
      }
    },
    handleAdd() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const res = await addTable(this.temp)
            if (res.code === 1) {
              this.$message({
                message: '新增成功',
                type: 'success'
              })
              this.dialogFormVisible = false
              this.getList()
            }
          } catch (error) {
            console.error('新增桌位失败:', error)
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const res = await updateTable(this.temp)
            if (res.code === 1) {
              this.$message({
                message: '更新成功',
                type: 'success'
              })
              this.dialogFormVisible = false
              this.getList()
            }
          } catch (error) {
            console.error('更新桌位失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该桌位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          try {
            const res = await deleteTable({ id: row.id })
            if (res.code === 1) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.getList()
            }
          } catch (error) {
            console.error('删除桌位失败:', error)
          }
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    showQrCode(row) {
      this.currentTable = Object.assign({}, row)
      this.currentQrCode = row.code || ''
      this.qrDialogVisible = true
    },
    async generateQrCode() {
      if (!this.currentTable.id) return
      
      this.qrLoading = true
      try {
        const res = await generateQrCode({ id: this.currentTable.id })
        if (res.code === 1 && res.data) {
          this.currentQrCode = res.data
          this.$message.success('二维码生成成功')
          
          // 更新列表中对应桌位的二维码
          const index = this.list.findIndex(item => item.id === this.currentTable.id)
          if (index !== -1) {
            this.list[index].code = res.data
          }
        }
      } catch (error) {
        console.error('生成二维码失败:', error)
        this.$message.error('生成二维码失败')
      } finally {
        this.qrLoading = false
      }
    },
    downloadQrCode() {
      if (!this.currentQrCode) return
      
      // 创建一个临时链接用于下载
      const link = document.createElement('a')
      link.href = this.currentQrCode
      link.download = `桌位_${this.currentTable.name}_二维码.png`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }
}
</script>

<style lang="scss" scoped>
.table-container {
  padding: 20px;
}

.action-container {
  margin-bottom: 20px;
  text-align: right;
}

.pagination-container {
  margin-top: 30px;
  text-align: center;
}

.qrcode-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qrcode-image {
  width: 200px;
  height: 200px;
  margin-bottom: 20px;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;

  img {
    max-width: 100%;
    max-height: 100%;
  }

  .no-qrcode {
    color: #999;
    font-size: 14px;
  }
}

.qrcode-info {
  margin-bottom: 20px;
  text-align: center;

  p {
    margin: 5px 0;
  }
}

.qrcode-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}
</style> 