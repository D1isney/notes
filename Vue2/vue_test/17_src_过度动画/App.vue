<template>
  <div>
    <Test></Test>
    <Test2></Test2>
    <Test3></Test3>
  </div>
</template>
<script>


import Test from "./components/Test.vue";
import Test2 from "./components/Test2.vue";
import Test3 from "./components/Test3.vue";

export default {

  name: 'App',
  components: {Test,Test2,Test3},
  data() {
    return {
    }
  },
  methods: {
  },
  watch: {
  },
  mounted() {
  },
  // 解绑
  beforeDestroy() {
  }
}
</script>
<style>

</style>