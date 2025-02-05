<template>
  <div class="app-container">
    <div class="log-table">
      <el-table
        :data="list"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        do-layout="doLayout"
        style="width: 100%"
        :default-sort="{prop: 'createTime', order: 'descending'}"
      >
        <el-table-column
          type="index"
          width="50"
        >
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-row>
                <el-col :span="18" :push="2">
                  <el-form-item label="内容：">
                    <span>{{ props.row.message }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="18" :push="2">
                  <el-form-item label="接口参数：">
                    <span>{{ props.row.params }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="18" :push="2">
                  <el-form-item label="返回参数：">
                    <span>{{ props.row.result }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column
          prop="createTime"
          label="调用时间"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="path"
          label="调用接口"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="调用用户"
        >
        </el-table-column>
        <el-table-column
          prop="type"
          sortable
          label="日志级别"
        >
          <template v-slot="{row}">
            <el-tag
              :type="typeTag(row.type)"
              effect="dark"
              size="small"
            >
              {{ (row.type || row.type === 0) && typeOptions[row.type].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="executeTime"
          label="执行时长(mm)"
        >
        </el-table-column>


      </el-table>
      <div class="button-box">
        <el-row>
          <el-col :span="7">
            <el-select v-model="type" placeholder="日志级别" clearable>
              <el-option
                v-for="item in optionsType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="9" :push="1">
            <el-date-picker
              v-model="createTime"
              type="date"
              placeholder="调用时间"
              style="width: 100%;"
              value-format="yyyy-MM-dd"
            />
          </el-col>

          <el-col :span="3" :push="3">
            <el-button type="primary" icon="el-icon-search" @click="getLogList">查询</el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getLogList"/>
    </div>
  </div>
</template>

<script>

import { getList, LogConst } from '@/api/log/logAPI'
import pagination from '@/components/Pagination/index'

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
        limit: 150,
        type: '',
        createTime: ''
      },
      total: 0,
      type: '',
      createTime: '',
      optionsType: [{
        value: '0',
        label: '普通日志'
      },{
        value: '1',
        label: '警告日志'
      },{
        value: '2',
        label: '危险日志'
      },{
        value: '3',
        label: '报警日志'
      }]
    }
  },
  computed: {
    typeOptions: () => Object.keys(LogConst.type).map(key => LogConst.type[key])
  },
  created() {
    this.getLogList()
  },
  methods: {
    typeTag(type) {
      if (type === 0) {
        return 'success'
      } else if (type === 1) {
        return 'warning'
      } else if (type === 2) {
        return 'warning'
      } else if (type === 3) {
        return 'danger'
      }
    },
    async getLogList() {
      this.query.type = this.type
      this.query.createTime = this.createTime
      await getList(this.query).then(res => {
        this.list = res.data.list
        this.total = res.data.totalCount
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);

  .el-drawer__body {
    overflow: auto;
  }

  .el-drawer__container ::-webkit-scrollbar {
    display: none;
  }
}

.log-table {
  width: 100%;
  height: calc(100% - 30px);
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
  margin-top: 1%;
}


</style>

