<template>
  <div class="school">
    <h2>学校名称：{{ name }}</h2>
    <button @click="sendSchoolName">点我把学校名传过去</button>
  </div>
</template>
<script>
import pubsub from 'pubsub-js'

export default {
  name: 'School',
  data() {
    return {
      name: 'Vue  学校123',
      address: '广州'
    }
  },
  methods: {
    sendSchoolName() {
      // this.$bus.$emit('hello',this.name)
      pubsub.publish("hello", this.name)
    }
  },
  mounted() {
    // console.log(this.x)
    // console.log("School：" + this.$on)
  }
}
</script>
<!--局部的-->
<style scoped>
.school {
  background-color: skyblue;
}
</style>