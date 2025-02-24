<template>
  <div class="logStatisticsView" ref="logStatisticsView" id="logStatisticsView">

  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex'
import * as echarts from 'echarts'
import { alarmStatistics } from '@/api/log/logAPI'

export default {
  name: 'logStatisticsView',
  data() {
    return {
      xAxisList: [],
      yAxisListAlarm: [],
      yAxisListWarning: []
    }
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      // 刷新inboundAndOutboundVolumeView
      if (val.type === 'logStatisticsView') {
        this.getAlarmStatistics()
      }
    }
  },
  methods: {
    ...mapActions('webSocket', ['openSocket']),
    initLogStatisticsView() {
      let logStatisticsView = echarts.init(this.$refs.logStatisticsView)
      let option
      option = {
        title: {
          text: '报警统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['警告', '报警']
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.xAxisList
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '警告',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            data: this.yAxisListWarning
          },
          {
            name: '报警',
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            data: this.yAxisListAlarm
          }
        ]
      }
      option && logStatisticsView.setOption(option)
      window.addEventListener('resize', function() {
        logStatisticsView.resize()
      })
    },

    async getAlarmStatistics() {
      await alarmStatistics().then(res => {
        if (res.code === 200){
          this.xAxisList = res.data.alarm.map(obj => obj.name)
          this.yAxisListAlarm = res.data.alarm.map(obj => obj.value)
          this.yAxisListWarning = res.data.warning.map(obj => obj.value)
          this.initLogStatisticsView();
        }
      })
    }
  },
  mounted() {
    this.getAlarmStatistics()
  }
}
</script>

<style lang="css" scoped>
.logStatisticsView {
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

