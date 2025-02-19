<template>
  <div class="surplusView" ref="surplus" id="surplus">

  </div>
</template>

<script>
import * as echarts from 'echarts'
import { inventoryBalance } from '@/api/inventory/inventoryAPI'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  name: 'surplusView',
  data() {
    return {
      surplusList: []
    }
  },
  methods: {
    initSurplus: function() {
      let surplus = echarts.init(this.$refs.surplus)
      let option = {
        title: {
          text: '库位余额',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        series: [
          {
            name: '剩余量',
            type: 'pie',
            radius: '50%',
            data: this.surplusList,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      // 绘制图表
      option && surplus.setOption(option)

      window.addEventListener('resize', function() {
        surplus.resize()
      })
    },

    ...mapActions('webSocket', ['openSocket']),
    async getSurplus() {
      await inventoryBalance().then(res => {
        if (res.code === 200 && res.data) {
          this.surplusList = res.data
        } else {
          this.surplusList = {
            name: '暂无库位可存',
            value: '0'
          }
        }
      })
      this.initSurplus()
    }
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      // 刷新surplusView
      if (val.type === 'surplusView') {
        this.getSurplus()
      }
    }
  },

  mounted() {
    this.getSurplus()
  },
  created() {
  }
}
</script>

<style lang="scss" scoped>
.surplusView {
  width: 30%;
  height: 90%;
  padding-top: 1%;
  padding-left: 1%;
  padding-right: 1%;
  box-shadow: 0px 0px 9px 0px rgba(210, 210, 210, 0.23) inset;
  background: linear-gradient(to left, #5d72ad, #5d72ad) left top no-repeat,
  linear-gradient(to bottom, #5d72ad, #5d72ad) left top no-repeat,
  linear-gradient(to left, #5d72ad, #5d72ad) right top no-repeat,
  linear-gradient(to bottom, #5d72ad, #5d72ad) right top no-repeat,
  linear-gradient(to left, #5d72ad, #5d72ad) left bottom no-repeat,
  linear-gradient(to bottom, #5d72ad, #5d72ad) left bottom no-repeat,
  linear-gradient(to left, #5d72ad, #5d72ad) right bottom no-repeat,
  linear-gradient(to left, #5d72ad, #5d72ad) right bottom no-repeat;
  background-size: 3px 12px, 12px 3px, 3px 12px, 12px 3px;
}
</style>

