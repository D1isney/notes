<template>
  <div class="app-container">
    <div class="member-table">
      <el-table
        :data="list"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        do-layout="doLayout"
        style="width: 100%"
        row-key="id"
        :expand-row-keys="expands"
        @row-click="openExpand"
        @expand-change="expandChange"
        @selection-change="handleSelectionChange"
        @current-change="handleSelectionChange"


        :header-cell-style="{ 'text-align': 'center' }"
        :cell-style="{ 'text-align': 'center' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="expand">
          <template slot-scope="propsList">
            <el-form label-position="left" :inline="false" class="demo-table-expand">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="ID：">
                    <span>{{ propsList.row.id }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="Username：">
                    <el-tag
                      type="success"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.username }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="Password：">
                    <el-tag
                      type="success"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.password }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="姓名：">
                    <el-tag
                      type="success"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.name }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="性别：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      {{ sexOptions[propsList.row.sex].label }}
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="年龄：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.age }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="邮箱：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.email }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="地址：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.address }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="生日：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.birthday }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建时间：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.createTime }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新时间：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.updateTime }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="过期时间：">
                    <el-tag
                      type="warning"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.expirationTime }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建人：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.createUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新人：">
                    <el-tag
                      type=""
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.updateUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="账号状态：">
                    <el-tag
                      :type="statusTag(propsList.row.status)"
                      effect="plain"
                      size="small"
                    >
                      {{ (propsList.row.status || propsList.row.status === 0) && statusOptions[propsList.row.status].label }}
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="加密盐：">
                    <el-tag
                      type="danger"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.salt }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="在线情况：">
                    <el-tag
                      :type="onlineTag(propsList.row.online)"
                      effect="dark"
                      size="small"
                    >
                      {{ onlineOptions[propsList.row.online].label }}
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>

            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="Username"
          prop="username"
        />
        <el-table-column
          label="过期时间"
          prop="expirationTime"
        />
        <el-table-column
          label="账号状态"
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
          label="登录状态"
        >
          <template v-slot="{row}">
            <el-tag
              :type="onlineTag(row.online)"
              effect="dark"
              size="small"
            >
              {{ (row.online || row.online === 0) && onlineOptions[row.online].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row>
              <el-col :span="15">
                <el-input v-model="query.param" placeholder="查询" clearable />
              </el-col>
              <el-col :span="7" :push="2">
                <el-button type="primary" icon="el-icon-search" @click="getList" />
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click.stop="openEditDrawer(scope.row)"/>
            <el-button type="danger" icon="el-icon-delete" circle @click.stop="handleDelete(scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <div class="button-box">
        <el-button type="primary" class="button-box-add" icon="el-icon-plus" @click.stop="openAddOrawer()" />
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid" @click.stop="deleteAll()" />
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList" />
    </div>
    <!--    修改-->
    <el-drawer
      title="修改用户"
      :visible.sync="modifyDrawer"
      direction="rtl"
    >
      <el-form label-position="right" label-width="80px" :model="modifyList">
        <el-form-item label="名称">
          <el-input v-model="modifyList.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="modifyList.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="modifyList.age" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="modifyList.email" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="modifyList.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="modifyList.address" />
        </el-form-item>
        <el-form-item label="生日">
          <el-col :span="24">
            <el-form-item>
              <el-date-picker
                v-model="modifyList.birthday"
                type="datetime"
                placeholder="time"
                style="width: 100%;"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-col :span="24">
            <el-form-item>
              <el-date-picker
                v-model="modifyList.expirationTime"
                type="datetime"
                placeholder="时间"
                style="width: 100%;"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="角色">
              <el-cascader v-model="selectedOptions" :options="options" :props="props" clearable collapse-tags placeholder="请选择角色" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="warning" @click="modifyMember()">修改</el-button>
          <el-button @click="modifyDrawer = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <!--    添加-->
    <el-drawer
      title="添加用户"
      :visible.sync="addDrawer"
      direction="rtl"
    >
      <el-form :ref="addMemberForm" :rules="rules" :model="addMemberForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="Username" prop="username">
              <el-input v-model="addMemberForm.username" placeholder="Username" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="Password" prop="password">
              <el-input v-model="addMemberForm.password" placeholder="Password" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="姓名">
              <el-input v-model="addMemberForm.name" placeholder="Name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="年龄">
              <el-input v-model="addMemberForm.age" placeholder="Age" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="出生日期">
              <el-date-picker
                v-model="addMemberForm.birthday"
                type="datetime"
                placeholder="Birthday"
                style="width: 100%;"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="性别">
              <el-radio-group v-model="addMemberForm.sex">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="邮箱">
              <el-input v-model="addMemberForm.email" placeholder="Email">
                <template slot="append">.com</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="电话">
              <el-input v-model="addMemberForm.phone" placeholder="Phone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="地址">
              <el-input v-model="addMemberForm.address" type="textarea" placeholder="Address" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="角色">
              <el-cascader v-model="selectedOptions" :options="options" :props="props" clearable collapse-tags placeholder="请选择角色" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="6">
            <el-button type="primary" @click="commitAdd()">添加</el-button>
            <el-button type="primary" @click="resetAddForm()">重置</el-button>
          </el-col>
        </el-row>

      </el-form>
    </el-drawer>
  </div>
</template>

<script>
import { deleteMember, getAllRole, getList, getRoleByMemberId, MemberConst, saveOrUpdate } from '@/api/member/member'
import pagination from '@/components/Pagination/index.vue'

export default {
  components: {
    pagination
  },
  data() {
    return {
      list: [],
      search: '',
      query: {
        page: 1,
        limit: 10,
        param: ''
      },
      total: 10,
      modifyDrawer: false,
      addDrawer: false,
      direction: 'rtl',
      //  修改的列表
      modifyList: [],
      // 多选的东西
      multipleSelection: [],

      props: { multiple: true },
      options: [],
      selectedOptions: [],
      addMemberForm: {
        username: '',
        password: '',
        name: '',
        sex: 1,
        email: '',
        phone: '',
        address: '',
        birthday: '',
        age: '',
        roleId: []
      },
      rules: {
        username: [
          { required: true, message: '请输入Username', trigger: 'change' },
          { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入Password', trigger: 'change' },
          { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
        ]
      },
      expands:[]
    }
  },
  computed: {
    statusOptions: () => Object.keys(MemberConst.status).map(key => MemberConst.status[key]),
    onlineOptions: () => Object.keys(MemberConst.online).map(key => MemberConst.online[key]),
    sexOptions: () => Object.keys(MemberConst.sex).map(key => MemberConst.sex[key])
  },
  created() {
    //  获取用户List
    this.getList()
  },
  methods: {
    handleDelete(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMember(row.id).then(res => {
          this.$message.success(res.message)
          this.getList()
        })
      }).catch(() => {
      })
    },
    async getList() {
      await getList(this.query).then(res => {
        this.list = res.data.list
        this.total = res.data.totalCount
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
    onlineTag(online) {
      if (online > 0) {
        return 'success'
      } else {
        return 'info'
      }
    },
    openEditDrawer(data) {
      this.modifyDrawer = true
      //  深拷贝
      this.modifyList = JSON.parse(JSON.stringify(data))
      this.getAllRole()
      this.getRoleByMemberId(this.modifyList.id)
    },
    async getRoleByMemberId(id) {
      getRoleByMemberId(id).then(res => {
        if (res.code === 200) this.selectedOptions = res.data.flat()
      })
    },
    async getAllRole() {
      //  拿到所有的角色
      await getAllRole().then(res => {
        if (res.code === 200) {
          this.options = res.data
        }
      })
    },
    modifyMember: async function() {
      this.modifyList.roleId = this.selectedOptions.flat()
      saveOrUpdate(this.modifyList).then(res => {
        if (res.code === 200) {
          this.$message({
            message: res.message,
            type: 'success'
          })
        } else {
          this.$message({
            message: res.message,
            type: 'warning'
          })
        }
      }).catch(e => {
      })
      this.modifyDrawer = false
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
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
    },
    deleteAll() {
      if (this.multipleSelection.length < 1) {
        this.$message.warning('请先勾选需要操作的用户')
        return
      }
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('此操作将永久删除已选中的用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMember(ids).then(res => {
          this.$message.success(res.message)
          if (this.list.length === ids.length && this.query.page > 1) this.query.page--
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    openAddOrawer() {
      this.addDrawer = true
      this.addMemberForm = {
        name: '',
        password: '',
        name: '',
        sex: 1,
        email: '',
        phone: '',
        address: '',
        birthday: '',
        age: '',
        roleId: []
      }
      this.getAllRole()
    },
    commitAdd() {
      this.addMemberForm.roleId = this.selectedOptions.flat()
      this.$refs[this.addMemberForm].validate((valid) => {
        if (valid) {
          this.addMemberForm.email = this.addMemberForm.email + '.com'
          saveOrUpdate(this.addMemberForm).then(res => {
            if (res.code === 200) {
              this.$message.success(res.message)
              this.addDrawer = false
              this.getList()
            } else {
              this.$message.warning(res.message)
            }
          })
        } else {
          return
        }
      })
    },
    resetAddForm() {
      this.selectedOptions = []
      this.addMemberForm = {
        name: '',
        password: '',
        name: '',
        sex: 1,
        email: '',
        phone: '',
        address: '',
        birthday: '',
        age: '',
        roleId: []
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
  /*1.显示滚动条：当内容超出容器的时候，可以滚动。：*/

  .el-drawer__body {
    overflow: auto;
    /* overflow-x: auto; */
  }

  .el-drawer__container ::-webkit-scrollbar {
    display: none;
  }
}
.member-table {
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

  .el-form_body-wrapper::-webkit-scrollbar {
    display: none;
  }

  .el-form_body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }
}

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

.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
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

</style>

