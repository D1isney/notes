<template>
  <div>
    <h2>学生名称：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
    <input type="text" v-fbind:value="name">
  </div>
</template>
<script>
export default {
  name: 'Student',
  data() {
    return {
      name: 'Vue',
      sex: '男'
    }
  },
  methods:{},
}
</script>
<style>

</style>【0