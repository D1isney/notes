<template>
  <div class="app">
    <h3>APP组件，{{ name }} -- {{ price }}</h3>
    <Child></Child>
  </div>
</template>
<script>

import Child from "./components/Child.vue";
import {provide, reactive, toRefs} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'App',
  setup() {
    let car = reactive({
      name: '奔驰',
      price: '400w'
    })
    //  给自己的后代组件传递数据
    provide("car", car)
    return {
      ...toRefs(car)
    }
  },
  components: {
    Child

  }
}
</script>
<style>
.app {
  background-color: #cccccc;
  padding: 10px;
}
</style>