<template>
  <div>
    <h2>学校名称：{{ name | mySlice }}</h2>
    <h2>学校地址：{{ address }}</h2>
    <button @click="test">点我触发Hello方法</button>
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
    test(){
      this.hello()
    }
  }
}
</script>
<style>

</style>