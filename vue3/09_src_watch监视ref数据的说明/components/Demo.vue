<template>
  <h2>当前求和为：{{ sum }}</h2>
  <button @click="sum++">点我+1</button>
  <hr>
  <h2>当前的信息为：{{ msg }}</h2>
  <button @click="msg+='!'">点我+！</button>
  <hr>
  <h2>姓名：{{ person.name }}</h2>
  <h2>年龄：{{ person.age }}</h2>
  <h2>薪资：{{ person.job.j1.salary }}</h2>
  <button @click="person.name += '~'">修改姓名</button>
  <button @click="person.age ++">增长年龄</button>
</template>
<script>
import {ref, watch} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)
    let msg = ref('Hello')

    let person = ref({
      name: '张三',
      age: 18,
      job: {
        j1: {
          salary: 20
        },
        j2: {
          salary: 30
        }
      }
    })

    watch(sum, (newValue, oldValue) => {
      console.log(newValue, oldValue)
    })

    watch(person, (newValue, oldValue) => {
      console.log(newValue, oldValue)
    }, {
      //  加深度监视或者监视的是person.value
      deep: true
    })


    return {
      sum, msg
      , person
    }

  }
}
</script>
<style>

</style>