<template>
  <h2>当前求和为：{{ sum }}</h2>
  <button @click="sum++">点我+1</button>
  <hr>
  <h2>当前的信息为：{{ msg }}</h2>
  <button @click="msg+='!'">点我+！</button>
</template>
<script>
import {ref, watch} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)
    let msg = ref('Hello')

    //  监视：情况一，监视ref所定义的响应式数据
    // watch(sum, (newValue, oldValue) => {
    //   console.log(newValue, oldValue)
    // })

    //  监视：情况二，监视ref所定义的多个响应式数据
    watch([sum, msg], (newValue, oldValue) => {
      console.log(newValue[0], oldValue[0])
      console.log(newValue[1], oldValue[1])
    },{
      // 这里写配置，上来就监视一次
      immediate:true,
      //  深度监视
      deep: true
    })

    return {
      sum, msg
    }

  }
}
</script>
<style>

</style>