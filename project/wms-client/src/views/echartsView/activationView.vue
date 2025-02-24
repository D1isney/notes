<template>
  <div class="activationView" ref="activationView" id="activationView">
  </div>
</template>
<script>
import { mapActions, mapGetters, mapState } from 'vuex'
import * as echarts from 'echarts'
import { averageRateOfActivity } from '@/api/task/taskAPI'

export default {
  name: 'activationView',
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
      // 刷新inboundAndOutboundVolumeView
      if (val.type === 'activationView') {
        this.getAverageRateOfActivity()
      }
    }
  },
  methods: {
    ...mapActions('webSocket', ['openSocket']),
    initActivationView() {
      let activationView = echarts.init(this.$refs.activationView)
      let option
      option = {
        title: {
          text: '稼动率统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {},
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            dataView: { readOnly: false },
            magicType: { type: ['line', 'bar'] },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.xAxisList
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value} %'
          }
        },
        series: [
          {
            name: '稼动率',
            type: 'line',
            data: this.yAxisList,
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' }
              ]
            },
            markLine: {
              data: [{ type: 'average', name: 'Avg' }]
            }
          }
        ]
      }
      option && activationView.setOption(option)
      window.addEventListener('resize', function() {
        activationView.resize()
      })
    },
    async getAverageRateOfActivity() {
      await averageRateOfActivity().then(res=>{
        if (res.code === 200 && res.data){
          this.xAxisList = res.data.map(obj => obj.name)
          this.yAxisList = res.data.map(obj => obj.value)
          this.initActivationView()
        }
      })
    }
  },
  mounted() {
    this.getAverageRateOfActivity()
  }
}
</script>
<style lang="css" scoped>
.activationView {
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
