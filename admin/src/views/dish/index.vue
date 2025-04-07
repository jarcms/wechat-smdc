<template>
  <div class="dish-container">
    <!-- 搜索栏 -->
    <div class="filter-container">
      <el-form :inline="true" :model="listQuery" class="demo-form-inline">
        <el-form-item label="菜品名称">
          <el-input v-model="listQuery.name" placeholder="请输入菜品名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="listQuery.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="listQuery.status" placeholder="请选择状态" clearable>
            <el-option label="起售" :value="1"></el-option>
            <el-option label="停售" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作栏 -->
    <div class="action-container">
      <el-button type="primary" @click="handleAdd">新增菜品</el-button>
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
      <el-table-column label="图片" width="100" align="center">
        <template slot-scope="scope">
          <el-image 
            style="width: 60px; height: 60px"
            :src="scope.row.image" 
            :preview-src-list="[scope.row.image]"
            fit="cover">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="菜品名称" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" width="100" align="center">
        <template slot-scope="scope">
          <span>¥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
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

    <!-- 新增/编辑菜品对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增菜品' : '编辑菜品'" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="temp.name" placeholder="请输入菜品名称"></el-input>
        </el-form-item>
        <el-form-item label="所属分类" prop="categoryId">
          <el-select v-model="temp.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜品价格" prop="price">
          <el-input-number v-model="temp.price" :precision="2" :step="0.1" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="菜品图片">
          <el-upload
            class="avatar-uploader"
            action="/admin/file/upload"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload">
            <img v-if="temp.image" :src="temp.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="菜品描述">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入菜品描述"></el-input>
        </el-form-item>
        <el-form-item label="销售状态">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">起售</el-radio>
            <el-radio :label="0">停售</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDishList, addDish, updateDish, deleteDish, updateDishStatus } from '@/api/dish'
import { getCategoryList } from '@/api/category'
import { getToken } from '@/utils/auth'

export default {
  name: 'DishManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        pageSize: 10,
        name: '',
        categoryId: '',
        status: ''
      },
      categoryOptions: [],
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        name: '',
        categoryId: '',
        price: 0,
        image: '',
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
      },
      uploadHeaders: {
        Authorization: getToken()
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const res = await getDishList(this.listQuery)
        if (res.code === 1) {
          this.list = res.data.records || []
          this.total = res.data.total || 0
        }
      } catch (error) {
        console.error('获取菜品列表失败:', error)
      } finally {
        this.listLoading = false
      }
    },
    async getCategoryOptions() {
      try {
        const res = await getCategoryList()
        if (res.code === 1) {
          this.categoryOptions = res.data || []
        }
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    handleSearch() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        pageSize: 10,
        name: '',
        categoryId: '',
        status: ''
      }
      this.getList()
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
        categoryId: '',
        price: 0,
        image: '',
        description: '',
        status: 1
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
            const res = await addDish(this.temp)
            if (res.code === 1) {
              this.$message({
                message: '新增成功',
                type: 'success'
              })
              this.dialogFormVisible = false
              this.getList()
            }
          } catch (error) {
            console.error('新增菜品失败:', error)
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const res = await updateDish(this.temp)
            if (res.code === 1) {
              this.$message({
                message: '更新成功',
                type: 'success'
              })
              this.dialogFormVisible = false
              this.getList()
            }
          } catch (error) {
            console.error('更新菜品失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该菜品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          try {
            const res = await deleteDish({ id: row.id })
            if (res.code === 1) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.getList()
            }
          } catch (error) {
            console.error('删除菜品失败:', error)
          }
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    async handleStatusChange(row) {
      try {
        const res = await updateDishStatus({ id: row.id, status: row.status })
        if (res.code !== 1) {
          // 如果更新失败，恢复原状态
          row.status = row.status === 1 ? 0 : 1
          this.$message.error('状态更新失败')
        } else {
          this.$message.success('状态更新成功')
        }
      } catch (error) {
        console.error('更新菜品状态失败:', error)
        // 如果发生异常，恢复原状态
        row.status = row.status === 1 ? 0 : 1
      }
    },
    handleUploadSuccess(res, file) {
      if (res.code === 1) {
        this.temp.image = res.data
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return (isJPG || isPNG) && isLt2M
    }
  }
}
</script>

<style lang="scss" scoped>
.dish-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.action-container {
  margin-bottom: 20px;
  text-align: right;
}

.pagination-container {
  margin-top: 30px;
  text-align: center;
}

.avatar-uploader {
  ::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    
    &:hover {
      border-color: #409EFF;
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.avatar {
  width: 150px;
  height: 150px;
  display: block;
}
</style> 