<template>
  <div class="app">
    <h3>APP组件</h3>
    <Suspense>
      <template v-slot:default>
        <Child></Child>
      </template>
      <template v-slot:fallback>
        <!--        退路-->
        <!--        上面那个组件不行了，就使用这个-->
        <h3>稍等....加载中</h3>
      </template>
    </Suspense>
  </div>
</template>
<script>
//  静态引入
// import Child from "./components/Child.vue";
import {defineAsyncComponent} from "vue";
//  动态引入、异步引入
const Child = defineAsyncComponent(() => {
  return import("./components/Child")
})
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