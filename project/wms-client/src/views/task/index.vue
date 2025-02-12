<template>
  <div class="app-container">
    <div class="member-table">
      <el-table
        ref="paramTable"
        :data="list"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        row-key="id"
        :expand-row-keys="expands"
        @row-click="openExpand"
        @expand-change="expandChange"
        @selection-change="handleSelectionChange"
      >
        @current-change="handleSelectionChange"
        style="width: 100%"
        >
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>

        <el-table-column type="expand">
          <template slot-scope="propsList">
            <el-form label-position="left" :inline="false" class="demo-table-expand">
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item
                    label="任务类型："
                  >
                    <el-tag
                      type="info"
                      :type="typeTag(propsList.row.type)"
                      effect="plain"
                      size="small"
                    >
                      {{ (propsList.row.type || propsList.row.type === 0) && statusOptions[propsList.row.type].label }}
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item
                    label="任务情况："
                  >
                    <el-tag
                      :type="statusTag(propsList.row.status)"
                      effect="dark"
                      size="small"
                    >
                    <span>
                         {{(propsList.row.status || propsList.row.status === 0) && statusOptions[propsList.row.status].label}}
                      </span>

                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="物料类型：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.goodsName }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="库存位置：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.inventoryName }} - {{ propsList.row.inventoryLayer }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="创建用户：">
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
                  <el-form-item label="更新用户：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.updateUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
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
              </el-row>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column
          label="任务编码"
          prop="code"
        />

        <el-table-column
          label="物料类型"
          prop="goodsName"
        />

        <el-table-column
          label="库存位置"
          prop="inventoryName"
        >
          <template v-slot="{row}">
            {{ row.inventoryName }} - {{ row.inventoryLayer }}
          </template>
        </el-table-column>

        <el-table-column
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row>
              <el-col :span="7">
                <el-input v-model="query.param" clearable placeholder="查询"></el-input>
              </el-col>
              <el-col :span="7" :push="1">
                <el-select v-model="query.type" placeholder="任务类型" clearable>
                  <el-option
                    v-for="item in optionsType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="7">
                <el-button type="primary" icon="el-icon-search" @click="getList"/>
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click.stop="openEditDrawer(scope.row)"/>
            <el-button type="danger" icon="el-icon-delete" circle @click.stop="deleteTask(scope.row)"/>
          </template>
        </el-table-column>

      </el-table>
      <div class="button-box">
        <el-button type="primary" class="button-box-add" icon="el-icon-plus" @click="openAddDrawer"/>
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid" @click="deleteAll"/>
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList"/>
    </div>
  </div>
</template>

<script>
import pagination from '@/components/Pagination/index.vue'
import { getTaskList, TaskConst } from '@/api/task/taskAPI'

export default {
  components: {
    pagination
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 20,
        param: '',
        type: ''
      },
      total: 20,
      list: [],
      expands: [],
      // 多选的东西
      multipleSelection: [],
      optionsType: []
    }
  },
  computed: {
    statusOptions: () => Object.keys(TaskConst.status).map(key => TaskConst.status[key]),
    typeOptions: () => Object.keys(TaskConst.type).map(key => TaskConst.type[key])
  },
  created() {
    this.getList()
    this.optionsType = TaskConst.type
  },
  methods: {
    async getList() {
      await getTaskList(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
        }
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
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    openEditDrawer(row) {
    },
    deleteTask(row) {
    },
    openAddDrawer() {

    },
    deleteAll() {
    },
    typeTag(type) {
      if (type === 4) {
        return 'success'
      } else {
        return 'warning'
      }
    },
    statusTag(status) {
      if (status === 0) {
        return ''
      } else if (status === 1) {
        return 'warning'
      } else if (status === 2) {
        return 'warning'
      } else if (status === 3) {
        return 'success'
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
  height: 7%;
  display: flex;
  justify-content: flex-end;
  padding-right: 0.5%;

  .button-box-add {
    width: 4%;
    height: 80%;
  }

  .button-box-delete {
    width: 4%;
    height: 80%;
  }
}

</style>

