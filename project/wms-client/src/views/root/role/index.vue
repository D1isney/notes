<template>
  <div class="app-container">
    <div class="role-table">
      <el-table
        ref="singleTable"
        :data="list"
        highlight-current-row
        height="93%"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column
          type="selection"
        >
        </el-table-column>
        <el-table-column
          property="code"
          label="编码"
        >
        </el-table-column>
        <el-table-column
          property="name"
          label="角色名称"
        >
        </el-table-column>
        <el-table-column
          property="createUsername"
          label="创建人"
        >
        </el-table-column>
        <el-table-column
          property="updateUsername"
          label="更新人"
        >
        </el-table-column>
        <el-table-column
          label="角色状态"
        >
          <template v-slot="{row}">
            <el-tag
              :type="statusTag(row.status)"
              effect="plain"
              size="small"
            >
              {{ (row.status || row.status === 0) && statusOptions[row.status].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          property="createTime"
          label="创建时间"
        >
        </el-table-column>
        <el-table-column
          property="updateTime"
          label="更新时间"
        >
        </el-table-column>
        <el-table-column
          property="remark"
          label="备注"
        >
        </el-table-column>
        <el-table-column
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="16">
                <el-input
                  v-model="param"
                  placeholder="查询"
                  clearable
                />
              </el-col>
              <el-col :span="8">
                <el-button type="primary" icon="el-icon-search" @click="getRoleList()"/>
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openEdit(scope.row)"
            >Edit
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >Delete
            </el-button>
          </template>
        </el-table-column>

      </el-table>
      <div class="button-box">
        <el-button type="primary" class="button-box-add" icon="el-icon-plus" @click="openDrawerAdd()"/>
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid" @click="deleteAll()"/>
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getRoleList"/>
    </div>
    <!--    修改角色-->
    <el-drawer
      title="修改角色"
      :visible.sync="drawerEdit"
      :direction="direction"
    >
      <el-form label-position="right" label-width="80px" :model="modifyList">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="名称">
              <el-input v-model="modifyList.name"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="编码">
              <el-input v-model="modifyList.code" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="角色状态">
              <el-radio-group v-model="modifyList.status">
                <el-radio :label="1">可用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="创建用户">
              <el-input v-model="modifyList.createUsername" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="更新用户">
              <el-input v-model="modifyList.updateUsername" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="创建时间">
              <el-input v-model="modifyList.createTime" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="更新时间">
              <el-input v-model="modifyList.updateTime" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="备注">
              <el-input type="textarea" v-model="modifyList.remark"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="22" :push="2">
            <el-form-item label="权限信息">
              <el-tag
                v-for="tag in modifyList.permissions"
                :key="tag"
                type="success"
              >
                {{ tag }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="6">
            <el-button type="primary" @click="modifyRole">修改</el-button>
            <el-button type="primary" @click="resetModify">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
    <!--    添加角色-->
    <el-drawer
      title="添加角色"
      :visible.sync="drawerAdd"
      :direction="direction"
    >
      <el-form label-position="right" label-width="80px" :model="addList" :rules="rules" :ref="addList">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="名称" prop="name">
              <el-input v-model="addList.name"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="角色状态">
              <el-radio-group v-model="addList.status">
                <el-radio :label="1">可用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="备注">
              <el-input type="textarea" v-model="addList.remark"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="6">
            <el-button type="primary" @click="addRole">保存</el-button>
            <el-button type="primary" @click="resetAdd">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>

  </div>
</template>

<script>
import {
  deleteRoleApi,
  getPermissionsByRoleId,
  getRoleListAPI,
  RoleConst,
  saveOrUpdateRoleAPI
} from '@/api/role/roleAPI'
import pagination from '@/components/Pagination/index'

export default {
  components: {
    pagination
  },
  data() {
    return {
      list: [],
      query: {
        page: 1,
        limit: 10,
        param: ''
      },
      param: '',
      total: 0,
      drawerEdit: false,
      drawerAdd: false,
      direction: 'rtl',
      modifyList: {
        permissions: []
      },
      addList: {
        name: '',
        status: 1,
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ]
      },
      multipleSelection: []
    }
  },
  computed: {

    statusOptions: () => Object.keys(RoleConst.status).map(key => RoleConst.status[key])
  },
  created() {
    this.getRoleList()
  },
  methods: {
    async getRoleList() {
      this.query.param = this.param
      console.log(this.query)
      await getRoleListAPI(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
        }
      })
    },
    statusTag(status) {
      if (status === 1) {
        return 'success'
      } else if (status === 0) {
        return 'danger'
      } else {
        return 'warning'
      }
    },
    //  展开修改
    openEdit(row) {
      //  通过该角色找到该角色的所有权限信息
      this.modifyList = {}
      this.modifyList = JSON.parse(JSON.stringify(row))
      getPermissionsByRoleId(row.id).then(res => {
        if (res.code === 200) {
          this.modifyList.permissions = res.data
          this.drawerEdit = true
        }
      })
    },
    modifyRole() {
      saveOrUpdateRoleAPI(this.modifyList).then(res => {
        if (res.code === 200) {
          this.drawerEdit = false
          this.getRoleList()
          this.$message.success(res.message)
        }
      })
    },
    resetModify() {
      this.modifyList = {
        name: '',
        code: '',
        status: 1,
        createUsername: '',
        updateUsername: '',
        createTime: '',
        updateTime: '',
        remark: ''
      }
    },
    // 单个删除
    handleDelete(row) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRoleApi(row.id).then(res => {
          if (res.code === 200) {
            this.getRoleList()
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
      this.drawerAdd = true
      this.resetAdd()
    },
    addRole() {
      this.$refs[this.addList].validate((valid) => {
        if (valid) {
          saveOrUpdateRoleAPI(this.addList).then(res => {
            if (res.code === 200) {
              this.$message.success(res.message)
              this.drawerAdd = false
              this.getRoleList()
            }
          })
        }
      })
    },
    resetAdd() {
      this.addList = {
        name: '',
        status: 1,
        remark: ''
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    deleteAll() {
      if (this.multipleSelection.length < 1) {
        this.$message.warning('请先勾选需要操作的角色')
        return
      }
      let ids = this.multipleSelection.map(item => item.id)
      this.$confirm('此操作将永久删除已选中的用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRoleApi(ids).then(res => {
          this.$message.success(res.message)
          if (this.list.length === ids.length && this.query.page > 1) this.query.page--
          this.getRoleList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

  }
}
</script>

<style scoped>
::v-deep ::-webkit-scrollbar {
  width: 6px;
  height: 10px;
  background-color: #ebeef5;
}

::v-deep ::-webkit-scrollbar-thumb {
  box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  background-color: #ccc;
}

::v-deep ::-webkit-scrollbar-track {
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 3px;
  background: rgba(255, 255, 255, 1);
}

.app-container {
  width: 100%;
  height: calc(100vh - 50px);

  .el-table__body-wrapper::-webkit-scrollbar {
    display: none; /* 对于Webkit浏览器 */
  }

  .el-table__body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }

  .el-form_body-wrapper::-webkit-scrollbar {
    display: none;
  }

  .el-form_body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }

  .el-drawer__body {
    overflow: auto;
    /* overflow-x: auto; */
  }

  .el-drawer__container ::-webkit-scrollbar {
    display: none;
  }
}

.role-table {
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
}

.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
}

.button-box {
  width: 100%;
  height: 7%;
  display: flex;
  justify-content: flex-end;
  padding-right: 0.5%;

  .button-box-add {
    width: 3%;
    height: 80%;
  //background-color: transparent; border: 0;
  }

  .button-box-add:hover {
    background: rgba(100, 255, 255, 255);
    transition: 0.5s;
  }

  .button-box-delete {
    width: 3%;
    height: 80%;
  }
}
</style>

