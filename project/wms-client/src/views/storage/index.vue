<template>
  <div class="app-container">
    <div class="inventory-box">
      <div class="color-tips">
        <div class="tips-div">
          <div class="tips-c-empty">
          </div>
          <div class="tips-c-blank"></div>
          <div class="tips-t">无货物</div>
        </div>
        <div class="tips-div">
          <div class="tips-c-have">
          </div>
          <div class="tips-c-blank"></div>
          <div class="tips-t">有货物</div>
        </div>
        <div class="tips-div">
          <div class="tips-c-in">
          </div>
          <div class="tips-c-blank"></div>
          <div class="tips-t">正在入库</div>
        </div>

        <div class="tips-div">
          <div class="tips-c-out">
          </div>
          <div class="tips-c-blank"></div>
          <div class="tips-t">正在出库</div>
        </div>

      </div>
      <Inventory ref="inventory" :setSelectData="getSelectData"></Inventory>
    </div>
    <div class="operation-box">
      <div class="input-box">
        <div class="input-form">
          <el-form ref="warehousingList" :label-position="labelPosition" label-width="150px" :model="warehousingList"
                   :rules="warehousingListRules"
          >
            <el-row :gutter="20">
              <el-col :span="18">
                <el-form-item label="物料编码：" prop="goodsCode">
                  <el-input v-model="warehousingList.goodsCode" clearable></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="18">
                <el-form-item label="库存编码：">
                  <el-input v-model="warehousingList.inventoryCode" clearable placeholder="手动编辑/自动生成"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="18">
                <el-form-item label="库位编码：">
                  <el-input v-model="warehousingList.storageCode" disabled clearable placeholder="自动生成"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>

        </div>
        <div class="input-buttons">
          <el-button type="primary" class="input-button-operation" icon="el-icon-caret-left" @click="manualWarehousing">
            手动入库
          </el-button>
          <el-button type="primary" class="input-button-operation" icon="el-icon-full-screen" @click="QR_Warehousing">
            扫码入库
          </el-button>
          <el-button type="primary" class="input-button-operation" icon="el-icon-caret-right" @click="outWarehousing">
            手动出库
          </el-button>
        </div>
      </div>
      <div class="button-box">
        <div class="buttons1">
          <el-button class="button1" icon="el-icon-sort" type="primary" @click="intelligentDiskLibrary">智能盘库
          </el-button>
          <el-button class="button1" icon="el-icon-s-operation" type="info" @click="storageMessage">库位信息
          </el-button>
          <el-button class="button1" icon="el-icon-s-operation" type="info" @click="openInventoryMessage">库存信息
          </el-button>
          <el-button class="button1" icon="el-icon-notebook-2" type="info" @click="openTaskMessage">任务信息
          </el-button>
        </div>
        <div class="logTable">
          <el-table
            :data="operationLogList"
            style="width: 100%"
            stripe
            height="100%"
            v-loading="logTableLoading"
            :header-cell-style="{ 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }"
          >
            <el-table-column
              type="index"
              width="40%"
            >
            </el-table-column>
            <el-table-column
              prop="message"
              label="任务日志"
            >
            </el-table-column>
            <el-table-column
              prop="result"
              label="结果"
            >
              <template v-slot="{row}">
                <el-tag
                  :type="typeTag(row.type)"
                >
                  {{ row.result }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

    </div>

    <el-dialog
      title="库位信息"
      :visible.sync="storageMessageVisible"
      width="80%"
    >
      <el-table
        :data="storageData"
        style="width: 100%;margin-bottom: 20px; display: flex;flex-direction: column"
        row-key="id"
        border
        default-expand-all
      >
        <el-table-column
          prop="code"
          label="库位编码"
          sortable
        >
          <template slot-scope="scope">
            <el-input v-model="scope.row.code" disabled placeholder="库位编码"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="水平位置"
          sortable
        >
          <template slot-scope="scope">
            <el-input v-model="scope.row.row" placeholder="库位名称"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="库位名称"
          sortable
        >
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" placeholder="库位名称"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          sortable
        >
        </el-table-column>
        <el-table-column
          label="是否禁用"
          sortable
        >
          <template slot-scope="scope">
            <el-switch
              style="display: block"
              v-model="scope.row.forbidden"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="可用"
              inactive-text="禁止"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          label="备注"
          sortable
        >
          <template slot-scope="scope">
            <el-input v-model="scope.row.remark" placeholder="备注"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <div class="updateStorageButtonBox">
        <el-button type="warning" class="updateStorageButton" @click="updateStorage">修改</el-button>
        <el-button type="info" class="updateStorageButton" @click="storageMessageVisible = false">取消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="库存信息"
      :visible.sync="inventoryMessageVisible"
      width="80%"
    >
      <el-row :gutter="20" v-for="(item,index) in this.inventoryListData">
        <el-col :span="24">
          <el-descriptions :title="item.storageName">
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-caret-top"></i>
                位置总数
              </template>
              {{ item.inventoryNum }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-caret-right"></i>
                位置余额
              </template>
              {{ item.inventoryResidue }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-caret-left"></i>
                已用数量
              </template>
              {{ item.inventoryUsed }}
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import Inventory from '@/views/inventory/index.vue'
import { intelligent, warehousing } from '@/api/inventory/inventoryAPI'
import {
  getStorageListAndGetInventoryList,
  getStorageListAPI, getTaskListAPI,
  queryInventoryNum,
  saveOrUpdateStorage
} from '@/api/storage/storageApi'
import pagination from '@/components/Pagination/index.vue'
import { getLogList, LogConst } from '@/api/log/logAPI'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  data() {
    return {
      warehousingList: {
        type: 0,
        goodsCode: '24122701',
        inventoryCode: '',
        storageCode: ''
      },
      warehousingListRules: {
        goodsCode: [
          { required: false, message: '请输入物料编码！！！', trigger: 'change' }
        ]
      },
      labelPosition: 'left',
      storageMessageVisible: false,
      inventoryMessageVisible: false,
      taskMessageVisible: true,
      storageData: [],
      query: {
        page: 1,
        limit: 20
      },
      taskQuery: {
        page: 1,
        limit: 10,
        operationType: 4
      },
      taskTotal: 10,
      inventoryListData: [],
      operationLogList: [],
      logTableLoading: false
    }
  },
  methods: {
    QR_Warehousing() {
      this.$notify({
        title: '失败',
        message: '请配置相应的扫码枪以启动该功能！！！',
        type: 'error'
      })
    },
    manualWarehousing() {
      let list = []
      list.push(this.warehousingList)
      this.$refs.warehousingList.validate((valid) => {
        if (valid) {
          warehousing(list).then(res => {
          })
        } else {
          return false
        }
      })
    },
    async intelligentDiskLibrary() {
      await intelligent().then(res => {
        if (res.code === 200) {
          this.$refs.inventory.$emit('intelligentDiskLibrary')
        }
      })
    },
    outWarehousing() {
      this.$refs.inventory.$emit('outWarehousing')
    },
    storageMessage() {
      this.getStorageList()
      this.storageMessageVisible = true
    },
    getStorageList() {
      getStorageListAPI(this.query).then(res => {
        if (res.code === 200) {
          this.storageData = res.data.list
        }
      })
    },
    async updateStorage() {
      await saveOrUpdateStorage(this.storageData).then(res => {
        if (res.code === 200) {
          this.storageMessageVisible = false
          // this.getStorageList()
          this.$message.success(res.message)
          this.$refs.inventory.$emit('intelligentDiskLibrary')
        }

      })

    },
    openInventoryMessage() {
      queryInventoryNum().then(res => {
        if (res.code === 200) {
          this.inventoryListData = res.data
          this.inventoryMessageVisible = true
        }
      })
    },
    openTaskMessage() {
      this.$router.push({ path: '/task' })
    },
    async getOperationLog() {
      await getLogList(this.taskQuery).then(res => {
        this.logTableLoading = true
        if (res.code === 200) {
          this.operationLogList = res.data.list
        }
        setTimeout(() => {
          this.logTableLoading = false
        }, 500)
      })
    },
    ...mapActions('webSocket', ['openSocket']),
    typeTag(type) {
      if (type === 0 || type === 4) {
        return 'success'
      } else if (type === 1 || type === 5) {
        return 'warning'
      } else if (type === 2) {
        return 'warning'
      } else if (type === 3) {
        return 'danger'
      }
    },

    getSelectData(data) {
      if (data.length > 0) {
        this.warehousingList.inventoryCode = data[data.length - 1].code
      }
      if (data.length <= 0){
        this.warehousingList.inventoryCode = ''
      }
    }
  },
  components: {
    Inventory, pagination
  },

  created() {
    this.getOperationLog()
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData']),
    typeOptions: () => Object.keys(LogConst.type).map(key => LogConst.type[key])
  },
  watch: {
    socketData(val) {
      if (val.type === 'operation') {
        this.getOperationLog()
      }
      if (val.type === 'log') {
        this.getOperationLog()
      }
    }
  },
  mounted() {
    this.$on('outWarehousing', (data) => {
    })
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

  .el-dialog__body {
    overflow: auto;
  }

  .el-dialog__container ::-webkit-scrollbar {
    display: none;
  }

  display: flex;
  flex-direction: column;
}


.inventory-box {
  width: 100%;
  height: 60%;
  display: flex;
  flex-direction: column;
  background-color: transparent;

  .color-tips {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
  }

  .tips-div {
    width: 10%;
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .tips-c-have {
  //flex-grow: 5; align-items: center; //height: 90%; width: 20px; height: 20px;
    background-color: var(--colorHave);
  }

  .tips-c-empty {
  //flex-grow: 5; align-items: center; //height: 90%; width: 20px; height: 20px;
    background-color: var(--colorEmpty);
  }

  .tips-c-in {
  //flex-grow: 5; align-items: center; //height: 90%; width: 20px; height: 20px;
    background-color: var(--colorIn);
  }

  .tips-c-out {
  //flex-grow: 5; align-items: center; //height: 90%; width: 20px; height: 20px;
    background-color: var(--colorOut);
  }


  .tips-c-blank {
    flex-grow: 2;
    align-items: center;
  }

  .tips-t {
    flex-grow: 25;
    align-items: center;
  }
}

.operation-box {
  width: 100%;
  height: 38%;
  margin-top: 2%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;

  .input-box {
    width: 55%;
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  //border: 1px solid #a6a1a1; //border-radius: 10px;

    .input-form {
      width: 70%;
      height: 100%;
      margin-top: 2%;
    //margin-left: 10%;
    }

    .input-buttons {
      width: 30%;
      height: 100%;
      display: flex;
      justify-content: space-around;
      flex-direction: column;

      .input-button-operation {
        width: 60%;
        height: 20%;
      }
    }
  }

  .button-box {
    width: 40%;
    height: 100%;
    display: flex;
    justify-content: space-around;
    flex-direction: row;

    .buttons1 {
      width: 30%;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      height: 100%;
    //border: 1px solid #cccccc; //border-radius: 10px; //background-color: #4b1717; margin-right: 5%; padding-left: 1%; padding-right: 1%;
    }

    .logTable {
      width: 60%;
      height: 100%;
      /* 2.el-table滚动条样式 */
      /* 滚动条轨道宽度17会出现间隙，直接body撑满 */

      .el-table__body-wrapper,
      .el-table__body {
        /* 权重不够，需要增加important */
        width: 100% !important;
      }

      /* 滚动条大小设置  */

      .el-table__body-wrapper::-webkit-scrollbar {
        width: 5px;
        height: 5px;
        border-radius: 10px;
        background-color: #ffffff !important;
      }

      /* 滚动条滑块样式设置 */

      .el-table__body-wrapper::-webkit-scrollbar-thumb {
        width: 5px;
        border-radius: 5px;
        background-color: #8f8f8f;
      }

      /* body宽度撑满后，表头会出现错位，增加下面样式覆盖gutter原始的宽度17 */

      .el-table__header colgroup col[name='gutter'],
      .el-table__body-wrapper colgroup col[name='gutter'] {
        display: table-cell;
      }

      .el-table th.gutter,
      colgroup.gutter {
        width: 5px !important;
        border-left: 1px solid transparent !important;
      }

      /* 隐藏表头最后一个单元格的右边框 */

      .has-gutter th.el-table__cell:nth-last-child(2) {
        /* 权重不够，需要增加important */
        border-right: none !important;
      }

    }

    .button1 {
      width: 100%;
    //border-radius: 10px;
    }
  }
}

/deep/ .el-button {
  margin-left: 0;
}

.updateStorageButtonBox {
  display: flex;
  justify-content: flex-end;
  flex-direction: row;
}

.updateStorageButton {
  margin-left: 2%;
}

</style>
