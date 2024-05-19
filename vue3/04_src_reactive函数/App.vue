<template>
  <h1>Vue3</h1>
  <h2>name:{{ name }}</h2>
  <h2>age:{{ age }}</h2>
  <h3>type:{{ job.type }}</h3>
  <h3>salary:{{ job.salary }}</h3>
  <h3>c:{{ student.a.b.c }}</h3>
  <h3>arr{{ arr }}</h3>
  <button @click="changeInfo">修改人的信息</button>
</template>

<script>
import {ref, reactive} from 'vue'

export default {
  name: 'App',

  setup() {
    let name = ref('张三')
    let age = ref(20)
    let job = ref({
      type: '前端',
      salary: '30k'
    })
    let student = reactive({
      name: 'Hello Vue3',
      msg: 'Test',
      a: {
        b: {
          c: 666
        }
      }
    })

    let arr = reactive([
        '1','2','3'
    ])

    function changeInfo() {
      console.log(name, age)
      name.value = 'Vue3';
      age.value = 23
      job.value.type = '后端'
      job.value.salary = '50k'
      student.a.b.c = 999
      arr[0] = '2'
      console.log(student)
    }


    //  返回一个对象
    return {
      name, age, job, student, arr,
      changeInfo
    }
  }
}
</script>

<style>
</style>
