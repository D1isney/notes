<template>
  <div>
    <button @click="isShow = !isShow">显示/隐藏</button>
    <transition-group>
      <h1 v-show="isShow" key="1">Vue！</h1>
      <h1 v-show="isShow" key="2">Vue！</h1>
    </transition-group>
  </div>
</template>
<script>
export default {
  name: 'Test',
  data() {
    return {
      isShow: true
    }
  },
  methods: {},
  components: {}
}
</script>
<style scoped>
h1 {
  background-color: #653d2e;
  //transition: 0.5s linear;
}

/**
  进入的起点
  离开的终点
 */
.v-enter, .v-leave-to {
  transform: translateX(-100%);
}

.v-enter-active, .v-leave-active {
  transition: 0.5s linear;
}

/**
  进入的终点
  离开的起点
*/
.v-enter-to, .v-leave {
  transform: translateX(0);
}


</style>