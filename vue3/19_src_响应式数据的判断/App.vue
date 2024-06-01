<template>
  <div class="app">
    <h3>APP组件</h3>
  </div>
</template>
<script>


import {isProxy, isReactive, isReadonly, isRef, reactive, readonly, ref} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'App',
  setup: function () {
    let car = reactive({
      name: '奔驰',
      price: '400w'
    })
    let car2 = readonly(car)
    let sum = ref(0)

    console.log(isRef(sum))
    console.log(isReactive(car))
    console.log(isReadonly(car2))
    console.log(isProxy(sum))
    console.log(isProxy(car2))

    return {
      car, car2, sum
    }
  },
  components: {}
}
</script>
<style>
.app {
  background-color: #cccccc;
  padding: 10px;
}
</style>