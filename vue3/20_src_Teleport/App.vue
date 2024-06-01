<template>
  <div class="app">
    <h3>APP组件</h3>
    <Child></Child>
  </div>
</template>
<script>
import Child from "./components/Child.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'App',
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