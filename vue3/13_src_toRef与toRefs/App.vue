<template>
  <div>
    <button @click="isShowDemo = !isShowDemo">切换隐藏</button>
    <Demo v-if="isShowDemo"></Demo>
  </div>
</template>

<script>
import Demo from './components/Demo.vue'
import {ref} from "vue";

export default {
  name: 'App',
  setup() {
    let isShowDemo = ref(true)

    return {
      isShowDemo
    }
  },
  components: {Demo}
}
</script>

<style>
</style>
