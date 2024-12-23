<template>
  <div class="app-container">
    <div class="member-table">
      <el-table
        :data="list.filter(data => !search || data.username.toLowerCase().includes(search.toLowerCase()))"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        do-layout="doLayout"
        style="width: 100%"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" :inline="false" class="demo-table-expand">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="ID：">
                    <span>{{ props.row.id }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="Username：">
                    <el-tag
                      type="success"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ props.row.username }}</span>
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
                      <span>{{ props.row.password }}</span>
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
                      <span>{{ props.row.name }}</span>
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
                      {{ sexOptions[props.row.sex].label }}
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
                      <span>{{ props.row.age }}</span>
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
                      <span>{{ props.row.email }}</span>
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
                      <span>{{ props.row.address }}</span>
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
                      <span>{{ props.row.birthday }}</span>
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
                      <span>{{ props.row.createTime }}</span>
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
                      <span>{{ props.row.updateTime }}</span>
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
                      <span>{{ props.row.expirationTime }}</span>
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
                      <span>{{ props.row.createUsername }}</span>
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
                      <span>{{ props.row.updateUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="账号状态：">
                    <el-tag
                      :type="statusTag(props.row.status)"
                      effect="plain"
                      size="small"
                    >
                      {{ statusOptions[props.row.status].label }}
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
                      <span>{{ props.row.salt }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="在线情况：">
                    <el-tag
                      :type="onlineTag(props.row.online)"
                      effect="dark"
                      size="small"
                    >
                      {{ onlineOptions[props.row.online].label }}
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
          label="邮箱"
          prop="email"
        />
        <el-table-column
          label="过期时间"
          prop="expirationTime"
        />
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
          <template slot="header">
            <el-input
              v-model="search"
              size="mini"
              placeholder="输入Username关键字搜索"
            />
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openAddDrawer(scope.row)"
            >Edit
              <!--              @click="handleEdit(scope.$index, scope.row)"-->
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
            >Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="button-box">
        <el-button type="primary" circle icon="el-icon-circle-plus-outline" class="button-box-add" />
        <el-button type="primary" icon="el-icon-circle-plus" circle class="button-box-delete" />
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList" />
    </div>

    <el-drawer
      title="我是标题"
      :visible.sync="drawer"
      direction="rtl"
    >
      <span>我来啦!</span>
    </el-drawer>
  </div>
</template>

<script>
import { getList, MemberConst } from '@/api/member'
import pagination from '@/components/Pagination/index.vue'

export default {
  components: {
    pagination
  },
  data() {
    return {
      list: [
        {
          id: '',
          username: '',
          password: '',
          name: '',
          sex: '',
          age: '',
          email: '',
          phone: '',
          address: '',
          birthday: '',
          createTime: '',
          updateTime: '',
          expirationTime: '',
          createMember: '',
          updateMember: '',
          status: '',
          slat: '',
          online: '',
          createUsername: '',
          updateUsername: ''
        }
      ],
      search: '',
      query: {
        page: 1,
        limit: 10,
        username: '',
        status: '',
        online: ''
      },
      total: 10,
      drawer: false,
      direction: 'rtl',
      modifyList: [
        {
          id: '',
          username: '',
          password: '',
          name: '',
          sex: '',
          age: '',
          email: '',
          phone: '',
          address: '',
          birthday: '',
          createTime: '',
          updateTime: '',
          expirationTime: '',
          createMember: '',
          updateMember: '',
          status: '',
          slat: '',
          online: '',
          createUsername: '',
          updateUsername: ''
        }
      ]
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
    handleDelete(index, row) {
      console.log(index, row)
    },
    async getList() {
      await getList(this.query).then(res => {
        this.list = res.data.list
        this.total = res.data.totalCount
      })
    },
    statusTag(status) {
      if (status > 0) {
        return 'success'
      } else {
        return 'danger'
      }
    },
    onlineTag(online) {
      if (online > 0) {
        return 'success'
      } else {
        return 'info'
      }
    },
    openAddDrawer(data) {
      this.drawer = true
      this.modifyList = data
      this.modifyList.online = 0
    },
    async getMemberById(member) {

    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
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
}

.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
}
.button-box{
  width: 100%;
  height: 7%;
  display: flex;
  justify-content: flex-end;
  padding-right: 0.5%;
  .button-box-add{
    width: 3%;
    height: 80%;
    //background-color: transparent;
    border: 0;
  }
  .button-box-add:hover{
    background: rgba(100, 255, 255, 255);
    transition: 0.5s;
  }
  .button-box-delete{
    width: 3%;
    height: 80%;
  }
}
</style>

