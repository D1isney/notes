<template>
  <div>
    <button @click="isShowDemo = !isShowDemo">切换隐藏</button>
    <Demo v-if="isShowDemo"></Demo>
    <hr>
    <Test></Test>
  </div>
</template>

<script>
import Demo from './components/Demo.vue'
import Test from './components/Test.vue'
import {ref} from "vue";

export default {
  name: 'App',
  setup() {
    let isShowDemo = ref(true)

    return {
      isShowDemo
    }
  },
  components: {Demo, Test}
}
</script>

<style>
</style>
