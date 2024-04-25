<template>
  <!--  组件的结构-->
  <div class="demo">
    <h2>学校名称：{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
    <button @click="showName">点我提示学校名称</button>
  </div>
</template>
<script>
//  组件交换的代码
export default {
// export default Vue.extend({
// const school = Vue.extend({
// export const school = Vue.extend({
  name: 'School',
  data() {
    return {
      name: 'Vue',
      address: '广州'
    }
  },
  methods: {
    showName() {
      console.log(this.name)
    }
  },
}
// })
// export { school}
// export default school
</script>
<style>
/*组件的样式*/
.demo {
  background-color: orange;
}
</style>