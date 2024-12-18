<template>
  <div class="app-container">
    <div class="member-table">
      <el-table
        :data="list"
        :highlight-current-row="true"
        class="table"
        style="width: 100%">
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
                    <span>{{ props.row.username }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="Password：">
                    <span>{{ props.row.password }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="姓名：">
                    <span>{{ props.row.name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="性别：">
                    <span>{{ props.row.sex }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="年龄：">
                    <span>{{ props.row.age }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="邮箱：">
                    <span>{{ props.row.email }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="地址：">
                    <span>{{ props.row.address }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="生日：">
                    <span>{{ props.row.birthday }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建时间：">
                    <span>{{ props.row.createTime }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新时间：">
                    <span>{{ props.row.updateTime }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="过期时间：">
                    <span>{{ props.row.expirationTime }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建人：">
                    <span>{{ props.row.createMember }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新人：">
                    <span>{{ props.row.updateMember }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="账号状态：">
                    <!--                  <span>{{ props.row.status }}</span>-->
                    {{ statusOptions[props.row.status].label }}
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="加密盐：">
                    <span>{{ props.row.salt }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="在线情况：">
                    {{ onlineOptions[props.row.online].label }}
                  </el-form-item>
                </el-col>
              </el-row>

            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="Name"
          prop="name">
        </el-table-column>
        <el-table-column
          label="Username"
          prop="username">
        </el-table-column>
        <el-table-column
          label="邮箱"
          prop="email">
        </el-table-column>
        <el-table-column
          label="过期时间"
          prop="expirationTime">
        </el-table-column>
        <el-table-column
          align="right">
          <template slot="header" slot-scope="scope">
            <el-input
              v-model="search"
              size="mini"
              placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">Edit
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList"/>
    </div>
  </div>
</template>

<script>
import {getList, MemberConst} from "@/api/member";
import pagination from "@/components/Pagination/index.vue";

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
          online: ''
        }
      ],
      search: '',
      query: {
        page: 1,
        limit: 30,
        name: '',
        code: '',
        type: '',
        keyName: '',
      },
      total: 0
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    async getList() {
      getList(null).then(res => {
        this.list = res.data.list
      })
    }
  },
  computed: {
    statusOptions: () => Object.keys(MemberConst.status).map(key => MemberConst.status[key]),
    onlineObj: () => MemberConst.online,
    onlineOptions: () => Object.keys(MemberConst.online).map(key => MemberConst.online[key])
  },
  created() {
    //  获取用户List
    this.getList();
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
}
.member-table{
  width: 100%;
  height: calc(100% - 30px);
  .table{
    width: 100%;
    height:  calc(100% - 30px);
  }
}
.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
}
</style>

