<template>
  <div class="student">
    <h2>学生名称：{{ name }}</h2>
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
  methods: {},
  mounted() {
    // console.log(this.x)
    this.$bus.$on('hello', (data) => {
      console.log("接收到了数据：" + data)
    })
  },
  beforeDestroy() {
    // 销毁前 解绑 不要占用
    this.$bus.$off('hello')
  }
}
</script>
<style lang="less" scoped>
.student {
  background-color: orangered;
}
</style>