<template>
  <div class="app-container">
    <div class="inventory-box">
      <Inventory></Inventory>
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
                  <el-input v-model="warehousingList.storageCode" clearable placeholder="手动编辑/自动生成"></el-input>
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
          <el-button type="primary" class="input-button-operation" icon="el-icon-caret-right">手动出库</el-button>
          <!--          <el-button type="primary" class="input-button">扫码枪入库</el-button>-->
        </div>

      </div>


      <div class="button-box">
        <div class="buttons1">
          <el-button class="button1" icon="el-icon-sort" type="primary">智能盘库</el-button>
          <el-button class="button1" icon="el-icon-s-operation" type="primary">库位信息</el-button>
          <el-button class="button1" icon="el-icon-s-operation" type="primary">库存信息</el-button>
        </div>
        <div class="buttons2">
          <el-button class="button1" icon="el-icon-menu" type="primary">订单任务</el-button>
          <el-button class="button1" icon="el-icon-menu" type="primary">任务日志</el-button>
          <el-button class="button1" icon="el-icon-menu" type="primary">操作日志</el-button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Inventory from '@/views/inventory/index.vue'

export default {
  data() {
    return {
      warehousingList: {
        goodsCode: '',
        inventoryCode: '',
        storageCode: ''
      },
      warehousingListRules: {
        goodsCode: [
          { required: false, message: '请输入物料编码！！！', trigger: 'change' }
        ]
      },
      labelPosition: 'left'
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
      this.$refs.warehousingList.validate((valid) => {
        if (valid) {
          alert('submit!')


        } else {
          return false
        }
      })
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
    }
  }
}

/deep/ .el-button {
  margin-left: 0;
}

</style>
