<template>
  <div class="app-container">
    <div class="permissions-table">
      <el-table
        ref="singleTable"
        highlight-current-row
        height="93%"
        :data="list"
        style="width: 100%"
        row-key="id"
        :expand-row-keys="expands"
        @row-click="openExpand"
        @expand-change="expandChange"
        @selection-change="handleSelectionChange"


        :header-cell-style="{ 'text-align': 'center' }"
        :cell-style="{ 'text-align': 'center' }"
      >
        <el-table-column type="selection" />
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="权限名称">
                    <el-tag type="success">
                      {{ props.row.name }}
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="权限">
                    <el-tag type="success">
                      {{ props.row.path }}
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="创建人">
                    <el-tag type="info">
                      {{ props.row.createUsername }}
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新人">
                    <el-tag>
                      {{ props.row.updateUsername }}
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建日期" sortable />
        <el-table-column prop="updateTime" label="更新时间" sortable />
        <el-table-column prop="code" label="权限编码" />

        <el-table-column
          prop="remark"
          label="描述"
          :filters="filtersRemark"
          :filter-method="filterTag"
          filter-placement="bottom-end"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.remark === '用户' ? 'danger' : 'success'"
              disable-transitions
            >{{ scope.row.remark }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-input
                  v-model="query.param"
                  placeholder="查询"
                  clearable
                />
              </el-col>
              <el-col :span="4" :push="2">
                <el-button type="primary" icon="el-icon-search" @click.stop="getPermissionsList()" />
              </el-col>
              <el-col :span="4" :push="2">
                <el-button type="primary" icon="el-icon-refresh" @click.stop="clearFilter()" />
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click.stop="openEdit(scope.row)"/>
            <el-button type="danger" icon="el-icon-delete" circle @click="deletePermissions(scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <div class="button-box">
        <el-button type="primary" class="button-box-add" icon="el-icon-plus" @click="openDrawerAdd()" />
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid" @click="deleteAll()" />
      </div>
    </div>

    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getPermissionsList" />
    </div>
    <el-drawer
      title="修改权限"
      :visible.sync="editDrawer"
      :direction="direction"
    >
      <el-form :ref="modifyPermissionsList" :model="modifyPermissionsList" label-width="80px" :rules="rules">
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="modifyPermissionsList.name" />
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="modifyPermissionsList.code" disabled />
        </el-form-item>
        <el-form-item label="权限">
          <el-input v-model="modifyPermissionsList.path" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-input v-model="modifyPermissionsList.createTime" disabled />
        </el-form-item>
        <el-form-item label="更新时间">
          <el-input v-model="modifyPermissionsList.updateTime" disabled />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="modifyPermissionsList.createUsername" disabled />
        </el-form-item>
        <el-form-item label="更新人">
          <el-input v-model="modifyPermissionsList.updateUsername" disabled />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="modifyPermissionsList.remark" />
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="modifyPermissions()">修改</el-button>
          <el-button type="info" @click="editDrawer = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <el-drawer
      title="添加权限"
      :visible.sync="addDrawer"
      :direction="direction"
    >
      <el-form :ref="addList" label-position="right" label-width="80px" :model="addList" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="权限名称" prop="name">
              <el-input v-model="addList.name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="权限" prop="path">
              <el-input v-model="addList.path" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="描述">
              <el-input v-model="addList.remark" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="6">
            <el-button type="primary" @click="addPermissions()">保存</el-button>
            <el-button type="info" @click="resetAdd">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
import {
  deletePermissionsAPI,
  getPermissionsListAPI,
  getRemarkAPI, restSuperPermissions,
  saveOrUpdateAPI
} from '@/api/permissions/permissionsAPI'
import pagination from '@/components/Pagination/index'

export default {
  components: {
    pagination
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 30,
        param: ''
      },
      total: 0,
      list: [
        {
          name: '',
          code: '',
          path: '',
          createTime: '',
          updateTime: '',
          createUsername: '',
          updateUsername: '',
          remark: ''
        }
      ],
      multipleSelection: [],
      filtersRemark: [],
      editDrawer: false,
      direction: 'rtl',
      modifyPermissionsList: {},
      rules: {
        name: [
          { required: true, message: '请输入权限名称', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入权限', trigger: 'blur' }
        ]
      },
      addDrawer: false,
      addList: {
        name: '',
        path: '',
        remark: ''
      },
      expands:[]
    }
  },
  computed: {},
  created() {
    this.getRemark()
    this.getPermissionsList()
  },
  methods: {
    async getRemark() {
      await getRemarkAPI().then(res => {
        if (res.code === 200) {
          this.filtersRemark = res.data
        }
      })
    },
    async getPermissionsList() {
      getPermissionsListAPI(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
        }
      })
    },
    clearFilter() {
      this.$refs.singleTable.clearFilter()
      this.query.param = ''
      this.getPermissionsList()
    },
    filterTag(value, row) {
      return row.remark === value
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    //  展开修改
    openEdit(row) {
      this.editDrawer = true
      this.modifyPermissionsList = row
    },
    modifyPermissions() {
      this.$refs[this.modifyPermissionsList].validate((valid) => {
        if (valid) {
          saveOrUpdateAPI(this.modifyPermissionsList).then(res => {
            if (res.code === 200) {
              this.getPermissionsList()
              this.getRemark()
              this.$message.success(res.message)
              this.editDrawer = false
            }
          })
        } else {
          return false
        }
      })
    },
    deletePermissions(row) {
      this.$confirm('此操作将永久删除该权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePermissionsAPI(row.id).then(res => {
          if (res.code === 200) {
            this.clearFilter()
            this.getRemark()
            this.getPermissionsList()
            this.$message.success(res.message)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    openDrawerAdd() {
      this.addDrawer = true
    },
    resetAdd() {
      this.addList = {
        username: '',
        path: '',
        remark: ''
      }
    },
    addPermissions() {
      this.$refs[this.addList].validate((valid) => {
        if (valid) {
          saveOrUpdateAPI(this.addList).then(res => {
            if (res.code === 200) {
              this.getPermissionsList()
              this.getRemark()
              this.addDrawer = false
              this.$message.success(res.message)
            }
          })
        } else {
          return false
        }
      })
    },
    deleteAll() {
      if (this.multipleSelection.length < 1) {
        this.$message.warning('请先勾选需要操作的权限')
        return
      }
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('此操作将永久删除已选中的权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePermissionsAPI(ids).then(res => {
          if (res.code === 200) {
            this.$message.success(res.message)
            this.clearFilter()
            if (this.list.length === ids.length && this.query.page > 1) this.query.page--
            this.getRemark()
            this.getPermissionsList()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    superPermissions() {
      restSuperPermissions().then(res => {
        console.log(res)
      })
    },
    openExpand: function(row, colum, event) {
      if (this.expands.includes(row.id)) {
        this.expands = this.expands.filter(value => value !== row.id)
      } else {
        this.expands.push(row.id)
      }
    },
    //  多个小箭头
    expandChange(row, expandedRows) {//
      let that = this
      if (expandedRows.length) {//此时展开
        that.expands = []
        if (row) {
          that.expands.push(row.id)
        }
      } else {//折叠
        that.expands = []
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
}

.permissions-table {
  width: 100%;
  height: calc(100% - 30px);

  /* 隐藏滚动条，但仍可滚动 */

  .el-table__body-wrapper::-webkit-scrollbar {
    display: none; /* 对于Webkit浏览器 */
  }

  .el-table__body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }

  .button-box {
    width: 100%;
    height: 6%;
    display: flex;
    justify-content: flex-end;
    padding-right: 0.5%;
    margin-top: .3%;

    .button-box-add {
      width: 4%;
      height: 100%;
    }

    .button-box-delete {
      width: 4%;
      height: 100%;
    }
  }
}
</style>

