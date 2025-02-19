<template>
  <div class="dashboard-container">
    <div class="dashboard-top">
      <inboundAndOutboundVolumeView></inboundAndOutboundVolumeView>
      <weekly-workload-view></weekly-workload-view>
    </div>
    <div class="dashboard-foot">
      <surplus-view></surplus-view>
      <div class="dashboard-foot-box1">2</div>
      <log-statistics-view></log-statistics-view>
    </div>
  </div>
</template>

<script>
import SurplusView from '@/views/echartsView/surplusView.vue'
import WeeklyWorkloadView from '@/views/echartsView/weeklyWorkloadView.vue'
import InboundAndOutboundVolumeView from '@/views/echartsView/inboundAndOutboundVolumeView.vue'
import LogStatisticsView from '@/views/echartsView/logStatisticsView.vue'

export default {
  name: 'Dashboard',
  components: { SurplusView, WeeklyWorkloadView, InboundAndOutboundVolumeView ,LogStatisticsView},
  computed: {},
  methods: {},
  mounted() {
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  width: 100%;
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;

  .dashboard-top {
    width: 98%;
    height: 50%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;


  }

  .dashboard-foot {
    width: 98%;
    height: 50%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;

    .dashboard-foot-box1 {
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

  }
}
</style>
