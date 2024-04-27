<template>
  <div class="school">
    <h2>学校名称：{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
    <button @click="sendSchoolName">把学校名称给APP</button>
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
  methods: {
    sendSchoolName() {
      this.getSchoolName(this.name);
    }
  }
  , props: ['getSchoolName']
}
</script>
<!--局部的-->
<style scoped>
.school {
  background-color: skyblue;
}
</style>