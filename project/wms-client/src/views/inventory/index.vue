<template>
  <div class="inventory-container" v-loading="inventoryLoading">
    <div class="box" ref="rowBox" v-for="(row,rowIndex) in this.crosswise">
      <el-tooltip v-for="(layer,layerIndex) in row.inventoryList"
                  :key="layerIndex"
                  class="item"
                  effect="light"
                  :content="content"
                  placement="top-start"
                  ref="tooltip"
      >
        <div class="inventory"
             ref="layerBox"
             @mouseenter="showTooltip(layer)"
             :class="{'highlighted': layer.isSelected}"
             @click="selectItem(rowIndex,layerIndex,layer)"
             :style="{backgroundColor : getStatusColor(layer.status)}"
        >
          <!--        {{ item }}-->
          <!--        {{ layer.status }}-->
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script>
import { getStorageListAndGetInventoryList } from '@/api/storage/storageApi'
import { mapGetters, mapState, mapActions } from 'vuex'
import { warehousing } from '@/api/inventory/inventoryAPI'

export default {
  data() {
    return {
      query: {
        page: 1,
        limit: 10
      },
      inventoryLoading: false,
      crosswise: [],
      boxWidth: '',
      inventoryQuery: {
        page: 1,
        limit: 50,
        storageId: 0
      },
      maxWidth: 0,
      maxHeight: 0,
      selectIndex: null,
      // 高亮选中的
      selectLayer: [],
      content: '123'
    }
  },
  methods: {
    getList: function() {
      this.selectLayer = []
      getStorageListAndGetInventoryList().then(res => {
        if (res.code === 200) {
          this.maxWidth = res.data.length
          this.crosswise = res.data.map(row => {
            if (row.inventoryList.length > 0) {
              row.inventoryList.forEach(layer => {
                this.$set(layer, 'isSelected', false) // 初始化 isSelected 状态
              })
            }
            return row
          })
          res.data.forEach(item => {
            if (!item.hasOwnProperty('inventoryList')) {
              item.inventoryList = []
            }
            if (item.inventoryList.length > this.maxHeight) {
              this.maxHeight = item.inventoryList.length
            }
          })
          this.resetSize()
        }
      })
    },
    resetSize() {
      this.$nextTick(() => {
        this.$refs.rowBox.forEach(row => {
          row.style.width = 100 / this.maxWidth - 3 + '%'
        })
        this.$refs.layerBox.forEach(layer => {
          layer.style.height = 100 / this.maxHeight - 5 + '%'
        })
      })
    },
    selectItem(rowIndex, layerIndex, layer) {
      const selectedItem = this.crosswise[rowIndex].inventoryList[layerIndex]
      // 切换选中状态
      this.$set(selectedItem, 'isSelected', !selectedItem.isSelected)
      // console.log('Row:', rowIndex, 'Layer:', layerIndex, 'Selected:', selectedItem.isSelected)
      if (selectedItem.isSelected) {
        this.selectLayer.push(layer)
      } else {
        this.selectLayer = this.selectLayer.filter(item => {
          return item.id !== layer.id
        })
      }
      // console.log(this.selectLayer)
    },
    getStatusColor(status) {
      // 根据 status 返回不同的背景颜色
      switch (status) {
        case 0:
          return '#cccccc'
        case 1:
          return '#32cd32'
        case 2:
          return '#4682b4'
        case 3:
          return '#4682b4'
        case 4:
          return '#475247'
        case 5:
          return '#757575'
        default:
          return '#cccccc' // 默认灰色
      }
    },
    ...mapActions('webSocket', ['openSocket']),

    intelligentDiskLibrary() {
      this.inventoryLoading = true
      this.getList()
      setTimeout(() => {
        this.inventoryLoading = false
      }, 2000)
    },
    showTooltip(layer) {
      if (layer.goods) {
        this.content = layer.goods.name + '-' + layer.goods.code
      } else {
        this.content = '库存编码' + '-' + layer.code
      }
    },

    outWarehousing() {
      if (this.selectLayer.length <= 0) {
        this.$message.info('请选择需要出库的库存位置！')
        return
      }
      let list = []
      for (let i = 0; i < this.selectLayer.length; i++) {
        if (this.selectLayer[i].goodsId <= 0) {
          this.$message.info(this.selectLayer[i].name + '-' + this.selectLayer[i].layer + '不存在物料，无法出库！')
          return
        }
        let data = {
          type: 1,
          goodsId: this.selectLayer[i].goodsId,
          inventoryCode: this.selectLayer[i].code
        }
        list.push(data)
      }
      warehousing(list).then(res => {
        // console.log(res)
      })

    }
  },
  created() {
    this.getList()
  },
  computed: {
    ...mapGetters(['sidebar', 'username']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      if (val.type === 'PlcConnectError') {
        this.$notify({
          title: '失败',
          message: val.message,
          type: 'error'
        })
      }
      if (val.type === 'operation') {
        this.getList()
      }
    }
  },
  mounted() {
    this.$on('intelligentDiskLibrary', () => {
      this.intelligentDiskLibrary()
    })
    //  出库
    this.$on('outWarehousing', () => {
      this.outWarehousing()
    })

  }
}
</script>

<style scoped>
.inventory-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

.box {
  width: 0%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  background-color: transparent;
//background-color: #f5f5f5;
}

.inventory {
  width: 100%;
  height: 20%;
  background-color: transparent;
  cursor: pointer;
//border: 2px #99a9bf solid;
}

.highlighted {
  width: 100%;
  height: 20%;

  border: 3px #0ae11e solid;
//background-color: yellow; //border-color: orange;
}

</style>
