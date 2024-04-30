<template>
  <div>
    <button @click="isShow = !isShow">显示/隐藏</button>
<!--        <h1 v-show="isShow" class="go">你好啊！</h1>-->
    <transition name="run">
      <h1 v-show="isShow" :appear="true">你好啊！</h1>
    </transition>
    <transition>
      <h1 v-show="isShow" :appear="true">Vue！</h1>
    </transition>
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
  background-color: orangered;
}

@keyframes disney {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}

/**
  vue的设计
  默认是 v
.v-enter-active {
  animation: disney 1s;
}
 */
.run-enter-active {
  animation: disney 1s;
}

.run-leave-active {
  animation: disney 1s reverse;
}
.v-enter-active {
  animation: disney 1s;
}

.v-leave-active {
  animation: disney 1s reverse;
}

.com {
  animation: disney 1s;
}

.go {
  animation: disney 1s reverse;
}

</style>