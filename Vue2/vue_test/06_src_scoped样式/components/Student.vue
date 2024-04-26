<template>
  <div class="demo">
    <h2 class="test">学生名称：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
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
<style lang="less" scoped>
.demo{
  background-color: orangered;
  .test{
    font-size: 20px;
  }
}
</style>