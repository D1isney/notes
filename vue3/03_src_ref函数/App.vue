<template>
  <h1>Vue3</h1>
  <h2>name:{{ name }}</h2>
  <h2>age:{{ age }}</h2>
  <h3>type:{{job.type}}</h3>
  <h3>salary:{{job.salary}}</h3>
  <button @click="changeInfo">修改人的信息</button>
</template>

<script>
import {ref} from 'vue'
export default {
  name: 'App',

  setup() {
    let name = ref('张三')
    let age = ref(20)
    let job = ref({
      type:'前端',
      salary:'30k'
    })
    function changeInfo() {
      console.log(name,age)
      name.value = 'Vue3';
      age.value = 23
      job.value.type = '后端'
      job.value.salary = '50k'
    }


    //  返回一个对象
    return {
      name, age,job,
      changeInfo
    }
  }
}
</script>

<style>
</style>
