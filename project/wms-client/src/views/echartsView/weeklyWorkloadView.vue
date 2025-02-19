<template>
  <div class="weeklyWorkloadView" ref="weeklyWorkloadView" id="weeklyWorkloadView">

  </div>
</template>
<script>

import * as echarts from 'echarts'
import { weeklyWorkload } from '@/api/task/taskAPI'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  name: 'weeklyWorkloadView',
  data() {
    return {
      xAxisList: [],
      yAxisList: []
    }
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      // 刷新surplusView
      if (val.type === 'weeklyWorkloadView') {
        this.getWeeklyWorkload()
      }
    }
  },
  methods: {
    ...mapActions('webSocket', ['openSocket']),
    async getWeeklyWorkload() {
      await weeklyWorkload().then(res => {
        if (res.code === 200 && res.data) {
          this.xAxisList = res.data.map(obj => obj.name)
          this.yAxisList = res.data.map(obj => obj.value)
          this.initWeeklyWorkloadView()
        }
      })
    },

    initWeeklyWorkloadView() {
      let weeklyWorkload = echarts.init(this.$refs.weeklyWorkloadView)
      let option
      option = {
        title: {
          text: '任务量',
          subtext: '',
          left: 'left'
        },
        xAxis: {
          type: 'category',
          data: this.xAxisList
        },
        yAxis: {
          type: 'value'
        },
        tooltip: {
          trigger: 'item'
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        series: [
          {
            name: '任务数',
            data: this.yAxisList,
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          }
        ]
      }
      // 绘制图表
      option && weeklyWorkload.setOption(option)
      window.addEventListener('resize', function() {
        weeklyWorkload.resize()
      })
    }
  },
  mounted() {
    this.getWeeklyWorkload()
  }
}
</script>

<style lang="css" scoped>
.weeklyWorkloadView {
  width: 46%;
  height: 90%;
  padding-top: 2%;
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
//border: 1px solid #152041;
}
</style>
