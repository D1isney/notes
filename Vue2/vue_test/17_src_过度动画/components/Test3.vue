<template>
  <div>
    <button @click="isShow = !isShow">显示/隐藏</button>
    <transition-group
        appear
        name="animate__animated animate__bounce"
        enter-active-class="animate__swing"
        leave-active-class="animate__backOutUp"
    >
      <h1 v-show="isShow" key="1">Vue！</h1>
    </transition-group>
  </div>
</template>
<script>
//  引入css
import 'animate.css'

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
/*transition: 0.5s linear;*/
}


</style>