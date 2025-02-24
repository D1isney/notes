<template>
  <div class="dashboard-container">
    <div class="dashboard-top">
      <inboundAndOutboundVolumeView></inboundAndOutboundVolumeView>
      <weekly-workload-view></weekly-workload-view>
    </div>
    <div class="dashboard-foot">
      <surplus-view></surplus-view>
      <activation-view></activation-view>
      <log-statistics-view></log-statistics-view>
    </div>
  </div>
</template>

<script>
import SurplusView from '@/views/echartsView/surplusView.vue'
import WeeklyWorkloadView from '@/views/echartsView/weeklyWorkloadView.vue'
import InboundAndOutboundVolumeView from '@/views/echartsView/inboundAndOutboundVolumeView.vue'
import LogStatisticsView from '@/views/echartsView/logStatisticsView.vue'
import ActivationView from '@/views/echartsView/activationView.vue'

export default {
  name: 'Dashboard',
  components: { SurplusView, WeeklyWorkloadView, InboundAndOutboundVolumeView ,LogStatisticsView,ActivationView},
  computed: {},
  methods: {},
  mounted() {}
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
    width: 95%;
    height: 50%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
  }

  .dashboard-foot {
    width: 100%;
    height: 50%;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
}
</style>
