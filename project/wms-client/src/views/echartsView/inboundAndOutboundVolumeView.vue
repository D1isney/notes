<template>
  <div class="inboundAndOutboundVolumeView" ref="inboundAndOutboundVolumeView" id="inboundAndOutboundVolumeView">

  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex'
import * as echarts from 'echarts'
import { inboundAndOutboundVolume } from '@/api/task/taskAPI'

export default {
  name: 'inboundAndOutboundVolumeView',
  data() {
    return {
      xAxisList:[],
      yAxisListIn:[],
      yAxisListOut:[],
    }
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData'])
  },
  watch: {
    socketData(val) {
      // 刷新inboundAndOutboundVolumeView
      if (val.type === 'inboundAndOutboundVolumeView') {
        this.getInboundAndOutboundVolume()
      }
    }
  },
  methods: {
    ...mapActions('webSocket', ['openSocket']),

    initInboundAndOutboundVolumeView(){
      let initInboundAndOutboundVolume = echarts.init(this.$refs.inboundAndOutboundVolumeView)
      let option;
      option = {
        title: {
          text: '出入库量'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['出库量', '入库量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
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
          type: 'value'
        },
        series: [
          {
            name: '入库量',
            type: 'line',
            stack: 'Total',
            smooth: true,
            data: this.yAxisListIn
          },
          {
            name: '出库量',
            type: 'line',
            stack: 'Total',
            smooth: true,
            data: this.yAxisListOut
          }
        ]
      };

      option && initInboundAndOutboundVolume.setOption(option)
      window.addEventListener('resize', function() {
        initInboundAndOutboundVolume.resize()
      })
    },

    async getInboundAndOutboundVolume(){
      await inboundAndOutboundVolume().then(res=>{
        if (res.code === 200){
          this.xAxisList = res.data.in.map(obj => obj.name)

          this.yAxisListIn = res.data.in.map(obj => obj.value)
          this.yAxisListOut = res.data.out.map(obj => obj.value)

          this.initInboundAndOutboundVolumeView();
        }
      })
    }
  },
  mounted() {
    this.getInboundAndOutboundVolume()
  }
}
</script>

<style lang="css" scoped>
.inboundAndOutboundVolumeView{

  width: 46%;
  height: 90%;
  padding-top: 2%;
  padding-left: 1%;
  padding-right: 1%;
  border: 0px;
//border: 1px solid #152041;
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
