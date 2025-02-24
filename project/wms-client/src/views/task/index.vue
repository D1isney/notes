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

        :header-cell-style="{ 'text-align': 'center' }"
        :cell-style="{ 'text-align': 'center' }"
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
                         {{ (propsList.row.status || propsList.row.status === 0) && statusOptions[propsList.row.status].label }}
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
                      <span>{{ propsList.row.inventoryName }}</span>
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
          label="任务名称"
          prop="name"
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
            {{ row.inventoryName }}
          </template>
        </el-table-column>

        <el-table-column
          align="right"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click.stop="openEditDrawer(scope.row)"/>
            <el-button type="warning" icon="el-icon-caret-bottom" circle @click.stop="issued(scope.row)"/>
            <el-button type="danger" icon="el-icon-delete" circle @click.stop="deleteTask(scope.row)"/>
          </template>
        </el-table-column>

      </el-table>
      <div class="button-box">
        <el-row style="display: flex;justify-content: flex-start;align-items: center">
          <el-col :span="13">
            <el-input v-model="query.param" clearable placeholder="查询"></el-input>
          </el-col>
          <el-col :span="9" :push="1">
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
        </el-row>
        <el-button type="primary" class="button-box-add" icon="el-icon-search" @click="getList"/>
        <el-button type="primary" class="button-box-add" icon="el-icon-plus" @click="openAddDrawer"/>
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid" @click="deleteAll"/>
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList"/>
    </div>


    <el-drawer
      title="添加任务"
      :visible.sync="addDrawer"
      :direction="direction"
    >
      <el-form ref="addTaskForm" :model="addTaskForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务名称" prop="name">
              <el-input v-model="addTaskForm.name" placeholder="任务名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务编码">
              <el-input v-model="addTaskForm.code" placeholder="自动生成" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务类型">
              <el-radio-group v-model="addTaskForm.type">
                <el-radio :label="4">入库</el-radio>
                <el-radio :label="5">出库</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="物料类型">
              <div class="block">
                <el-cascader
                  v-model="goodsValue"
                  :options="goodsOptionsSelect"
                  @change="goodsHandleChange"
                ></el-cascader>
              </div>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="选择库位">
              <div class="block">
                <el-cascader
                  v-model="inventoryValue"
                  :options="inventoryOptionsSelect"
                  @change="inventoryHandleChange"
                ></el-cascader>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="是否下发">
              <el-switch
                v-model="addTaskForm.directlyIssued"
                active-text="直接下发"
                inactive-text="后续下发"
              >
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="备注">
              <el-form-item v-model="addTaskForm.remark">
                <el-input v-model="addTaskForm.remark" type="textarea" placeholder="备注"/>
              </el-form-item>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-row :gutter="20">
        <el-col :span="16" :push="6">
          <el-button type="warning" @click="commitAdd()">添加</el-button>
          <el-button type="info" @click="resetAddForm()">重置</el-button>
        </el-col>
      </el-row>
    </el-drawer>

    <el-drawer
      title="修改任务"
      :visible.sync="updateDrawer"
      :direction="direction"
    >
      <el-form ref="addTaskForm" :model="updateTaskForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务名称" prop="name">
              <el-input v-model="updateTaskForm.name" placeholder="任务名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务编码">
              <el-input v-model="updateTaskForm.code" placeholder="自动生成" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="任务类型">
              <el-radio-group v-model="updateTaskForm.type">
                <el-radio :label="4">入库</el-radio>
                <el-radio :label="5">出库</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="物料类型">
              <div class="block">
                <el-cascader
                  v-model="goodsUpdateValue"
                  :options="goodsOptionsSelect"
                  @change="goodsUpdateHandleChange"
                ></el-cascader>
              </div>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="选择库位">
              <div class="block">
                <el-cascader
                  v-model="inventoryUpdateValue"
                  :options="inventoryOptionsSelect"
                  @change="inventoryUpdateHandleChange"
                ></el-cascader>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="是否下发">
              <el-switch
                v-model="updateTaskForm.directlyIssued"
                active-text="直接下发"
                inactive-text="后续下发"
              >
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item
              label="任务情况"
            >
              <el-tag
                :type="statusTag(updateTaskForm.status)"
                effect="dark"
                size="small"
              >
                    <span>
                         {{
                        (updateTaskForm.status || updateTaskForm.status === 0) && statusOptions[updateTaskForm.status].label
                      }}
                      </span>

              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="创建用户" prop="createUsername">
              <el-input v-model="updateTaskForm.createUsername" placeholder="创建用户" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="更新用户" prop="updateUsername">
              <el-input v-model="updateTaskForm.updateUsername" placeholder="更新用户" disabled/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="创建时间" prop="createTime">
              <el-input v-model="updateTaskForm.createTime" placeholder="创建时间" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="更新时间" prop="updateTime">
              <el-input v-model="updateTaskForm.updateTime" placeholder="更新时间" disabled/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="备注">
              <el-input v-model="updateTaskForm.remark" type="textarea" placeholder="备注"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="16" :push="6">
            <el-button type="warning" @click="commitUpdate()">修改</el-button>
            <el-button type="info" @click="resetUpdateForm()">取消</el-button>
          </el-col>
        </el-row>

      </el-form>
    </el-drawer>

  </div>
</template>

<script>
import pagination from '@/components/Pagination/index.vue'
import {
  deleteTask,
  getGoodsAndInventory,
  getTaskList,
  manualOperationIssued,
  saveOrUpdateTask,
  TaskConst
} from '@/api/task/taskAPI'
import { deleteGoods, getBillOfMaterial } from '@/api/goods/goodsAPI'
import { getBillOfInventory } from '@/api/inventory/inventoryAPI'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  components: {
    pagination
  },
  data() {
    return {
      goodsValue: [],
      goodsUpdateValue: [],
      inventoryValue: [],
      inventoryUpdateValue: [],
      goodsOptionsSelect: [],
      inventoryOptionsSelect: [],
      addDrawer: false,
      updateDrawer: false,
      addTaskForm: {
        name: '',
        code: '',
        type: 4,
        directlyIssued: false,
        resource: 1,
        taskDataDTOS: {
          goodsCode: '',
          inventoryCode: '',
          storageCode: ''
        },
      },
      direction: 'rtl',
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
      optionsType: [],
      updateTaskForm: {
        name: '',
        code: '',
        type: '',
        directlyIssued: false,
        resource: 1,
        taskDataDTOS: {
          goodsCode: [],
          inventoryCode: [],
          storageCode: []
        },
      }
    }
  },
  computed: {
    statusOptions: () => Object.keys(TaskConst.status).map(key => TaskConst.status[key]),
    ...mapGetters(['sidebar', 'username']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      if (val.type === 'operation') {
        this.getList()
      }
    }
  },
  created() {
    this.getList()
    this.optionsType = TaskConst.type
  },
  methods: {
    ...mapActions('webSocket', ['openSocket']),
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
    issued(row) {
      if (row.status === 3) {
        this.$message.warning('该任务已完成！不能再次下发！')
        return
      }
      if (row.status !== 0) {
        this.$message.warning('该任务已下发！不能再次下发！')
        return
      }
      manualOperationIssued(row).then(res => {
        if (res.code === 200) {
          this.getList()
        } else {
          this.$message.error(row.message)
        }
      })
    },
    openEditDrawer(row) {
      this.goodsUpdateValue = []
      this.inventoryUpdateValue = []
      // this.goodsOptionsSelect = []
      // this.inventoryOptionsSelect = []
      this.updateTaskForm = row
      //  列表 数据
      getBillOfMaterial().then(res => {
        if (res.code === 200) {
          this.goodsOptionsSelect = res.data
        }
      })
      getBillOfInventory().then(res => {
        if (res.code === 200) {
          this.inventoryOptionsSelect = res.data
        }
      })
      getGoodsAndInventory(row).then(res => {
        if (res.code === 200) {
          this.inventoryUpdateValue = res.data.inventoryCode
          this.goodsUpdateValue = res.data.goodsCode
          if (this.updateTaskForm.hasOwnProperty('taskDataDTOS')) {
            this.updateTaskForm.taskDataDTOS = {
              goodsCode: '',
              inventoryCode: '',
              storageCode: ''
            }
          } else {
            let taskDataDTOS = {
              goodsCode: '',
              inventoryCode: '',
              storageCode: ''
            }
            this.updateTaskForm.push(taskDataDTOS)
          }

          this.updateTaskForm.taskDataDTOS.goodsCode = res.data.goodsCode
          // this.updateTaskForm.taskDataDTOS.inventoryCode = res.data.inventoryCode
        }
      })
      this.updateDrawer = true
    },

    deleteTask(row) {
      this.$confirm('此操作将永久删除该任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTask(row.id).then(res => {
          if (res.code === 200) {
            this.$message.success(res.message)
          }
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    openAddDrawer() {
      this.addTaskForm = {
        username: '',
        code: '',
        type: 4,
        directlyIssued: false,
        taskDataDTOS: {
          goodsCode: '',
          inventoryCode: '',
          storageCode: ''
        }
      }
      this.goodsValue = []
      this.inventoryValue = []
      // this.goodsOptionsSelect = []
      // this.inventoryOptionsSelect = []
      //  列表 数据
      getBillOfMaterial().then(res => {
        if (res.code === 200) {
          this.goodsOptionsSelect = res.data
        }
      })
      getBillOfInventory().then(res => {
        if (res.code === 200) {
          this.inventoryOptionsSelect = res.data
        }
      })
      this.addDrawer = true
    },
    goodsHandleChange(val) {
      //  物料编码
      this.addTaskForm.taskDataDTOS.goodsCode = val[1]
    },
    goodsUpdateHandleChange(val) {
      //  物料编码
      this.updateTaskForm.taskDataDTOS.goodsCode = val[1]
    },
    inventoryHandleChange(val) {
      this.addTaskForm.taskDataDTOS.storageCode = val[0]
      this.addTaskForm.taskDataDTOS.inventoryCode = val[1]
    },
    inventoryUpdateHandleChange(val) {
      this.updateTaskForm.taskDataDTOS.storageCode = val[0]
      this.updateTaskForm.taskDataDTOS.inventoryCode = val[1]
    },
    commitAdd() {
      console.log(this.addTaskForm)
      saveOrUpdateTask(this.addTaskForm).then(res => {
        if (res.code === 200) {
          this.$message.success(res.message)
          this.getList()
          this.addDrawer = false
        } else {
          this.$message.warning(res.message)
          return
        }
      })
    },
    commitUpdate() {
      if (this.updateTaskForm.status === 3) {
        this.$message.warning('该任务已完成！不能再次修改！')
        return
      }
      if (this.updateTaskForm.status !== 0) {
        this.$message.warning('该任务已下发！不能再次修改！')
        return
      }
      saveOrUpdateTask(this.updateTaskForm).then(res => {
        if (res.code === 200) {
          this.$message.success(res.message)
          this.getList()
          this.updateDrawer = false
        } else {
          this.$message.warning(res.message)
          return
        }
      })
    },
    resetUpdateForm() {
      this.updateDrawer = false
    },

    resetAddForm() {
      this.$refs.addTaskForm.resetFields()
    },
    deleteAll() {
      if (this.multipleSelection.length < 1) {
        this.$message.warning('请先勾选需要操作的物料')
        return
      }
      const ids = this.multipleSelection.map(item => item.id)
      this.$confirm('此操作将永久删除已选中的任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTask(ids).then(res => {
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

