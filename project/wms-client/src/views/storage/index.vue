<template>
  <div class="app-container">
    <div class="inventory-box">
      <Inventory ref="inventory"></Inventory>
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

        </div>
        <div class="buttons2">
          <el-button class="button1" icon="el-icon-s-operation" type="primary" @click="storageMessage">库位信息
          </el-button>
          <el-button class="button1" icon="el-icon-s-operation" type="primary" @click="openInventoryMessage">库存信息
          </el-button>
          <el-button class="button1" icon="el-icon-notebook-2" type="primary" @click="openTaskMessage">订单信息</el-button>
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
        <el-button type="primary" class="updateStorageButton" @click="updateStorage">修改</el-button>
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

    <el-dialog
      title="任务信息"
      :visible.sync="taskMessageVisible"
      width="80%"
    >
    </el-dialog>
  </div>
</template>

<script>
import Inventory from '@/views/inventory/index.vue'
import { intelligent, warehousing } from '@/api/inventory/inventoryAPI'
import {
  getStorageListAndGetInventoryList,
  getStorageListAPI,
  queryInventoryNum,
  saveOrUpdateStorage
} from '@/api/storage/storageApi'

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
      inventoryListData: []
    }
  },
  methods: {
    QR_Warehousing() {
      this.$notify({
        title: '失败',
        message: '暂不支持扫码枪入库功能，请及时联系管理员！！！',
        type: 'error'
      })
    },
    manualWarehousing() {
      let list = []
      list.push(this.warehousingList)
      this.$refs.warehousingList.validate((valid) => {
        if (valid) {
          warehousing(list).then(res => {
            // console.log(res)
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
    updateStorage() {
      console.log(this.storageData)
      saveOrUpdateStorage(this.storageData).then(res => {
        if (res.code === 200) {
          this.storageMessageVisible = false
          // this.getStorageList()
          this.$message.success(res.message)
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
    openTaskMessage(){
      this.taskMessageVisible = true
    }
  },
  components: {
    Inventory
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

  .el-dialog__body{
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
  background-color: transparent;
}

.operation-box {
  width: 100%;
  height: 39%;
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

    .input-form {
      width: 70%;
      height: 100%;
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
    flex-direction: column;

    .buttons1 {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      height: 20%;
    }

    .buttons2 {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      height: 20%;
    }

    .button1 {
      width: 30%;
      height: 100%;
      border-radius: 10px;
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
