<template>
  <div class="inventory-container" v-loading="inventoryLoading">
    <div class="box" ref="rowBox" v-for="(row,rowIndex) in this.crosswise">
      <div class="inventory"
           ref="layerBox"
           v-for="(layer,layerIndex) in row.inventoryList"
           :key="layerIndex"
           :class="{'highlighted': layer.isSelected}"
           @click="selectItem(rowIndex,layerIndex,layer)"
           :style="{backgroundColor : getStatusColor(layer.status)}"
      >
        <!--        {{ item }}-->
        <!--        {{ layer.status }}-->
      </div>
    </div>
  </div>
</template>

<script>
import { getStorageListAndGetInventoryList } from '@/api/storage/storageApi'

export default {
  computed: {},
  data() {
    return {
      query: {
        page: 1,
        limit: 10
      },
      inventoryLoading: true,
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
      selectLayer: []
    }
  },
  methods: {
    getList: function() {
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
          this.inventoryLoading = false
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
          return '#dedede'
        case 1:
          return '#4682b4'
        case 2:
          return '#32cd32'
        case 3:
          return '#4682b4'
        case 4:
          return '#32cd32'
        case 5:
          return '#bbbbbb'
        default:
          return '#cccccc' // 默认灰色
      }
    }
  },
  created() {
    this.getList()
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
