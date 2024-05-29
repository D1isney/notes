<template>
  <h2>当前求和为：{{ sum }}</h2>
  <button @click="sum++">点我+1</button>
</template>
<script>
import {onBeforeMount, onBeforeUnmount, onBeforeUpdate, onMounted, onUnmounted, onUpdated, ref} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)

    // 通过组合式API的形式去使用声明周期钩子
    /*
     beforeCreate、create => setup()
     beforeMount => onBeforeMount()
     mounted => onMounted
     beforeUpdate => onBeforeUpdate
     updated => onUpdated
     beforeUnmount => onBeforeUnmount
     unmounted => onUnmounted
    * */

    onBeforeMount(()=>{
      console.log("onBeforeMount")
    })
    onMounted(()=>{
      console.log("onMounted")
    })
    onBeforeUpdate(()=>{
      console.log("onBeforeUpdate")
    })
    onUpdated(()=>{
      console.log("onUpdated")
    })
    onBeforeUnmount(()=>{
      console.log("onBeforeUnmount")
    })
    onUnmounted(()=>{
      console.log("onUnmounted")
    })


    return {
      sum
    }
  },
  // beforeCreate() {
  //   console.log("beforeCreate")
  // },
  // created() {
  //   console.log("created")
  // },
  // beforeMount() {
  //   console.log("beforeMount")
  // },
  // mounted() {
  //   console.log("mounted")
  // },
  // beforeUpdate() {
  //   console.log("beforeUpdate")
  // },
  // updated() {
  //   console.log("updated")
  // },
  // beforeUnmount() {
  //   console.log("beforeUnmount")
  // },
  // unmounted() {
  //   console.log("unmounted")
  // }
}
</script>
<style>

</style>