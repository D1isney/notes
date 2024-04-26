<template>
  <div class="demo">
    <h2>学校名称：{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
  </div>
</template>
<script>
export default {
  name: 'School',
  data() {
    return {
      name: 'Vue  学校123',
      address: '广州'
    }
  },
  methods:{

  }
}
</script>
<!--局部的-->
<style scoped>
.demo{
  background-color: skyblue;
}
</style>