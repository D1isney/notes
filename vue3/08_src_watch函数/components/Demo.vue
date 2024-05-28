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
import {ref, reactive, watch} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)
    let msg = ref('Hello')

    let person = reactive({
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

    //  监视：情况一，监视ref所定义的响应式数据
    // watch(sum, (newValue, oldValue) => {
    //   console.log(newValue, oldValue)
    // })

    //  监视：情况二，监视ref所定义的多个响应式数据
    // watch([sum, msg], (newValue, oldValue) => {
    //   console.log(newValue[0], oldValue[0])
    //   console.log(newValue[1], oldValue[1])
    // }, {
    //   // 这里写配置，上来就监视一次
    //   immediate: true,
    //   //  深度监视
    //   deep: true
    // })

    //  监视：情况三，监视reactive所定义的多个响应式数据
    //  如果用的reactive所定义的，用watch监视的，就无法获取oldValue
    // watch(person, (newValue, oldValue) => {
    //   // oldValue的值也是newValue
    //   console.log(newValue, oldValue)
    // }, {
    //   //  关不掉深度监视，强制开启deep
    //   deep: false
    // })

    //  监视：情况四，监视reactive所定义的多个响应式数据中的某个属性
    // watch(() => person.age, (newValue, oldValue) => {
    //   // oldValue的值也是newValue
    //   console.log(newValue, oldValue)
    // }, {
    //   //  关不掉深度监视，强制开启deep
    //   deep: false
    // })

    //  监视：情况四，监视reactive所定义的多个响应式数据中的某些属性
    // watch(() => [person.age, person.name], (newValue, oldValue) => {
    //   console.log(newValue[0], oldValue[0])
    //   console.log(newValue[1], oldValue[1])
    // }, {
    //   //  关不掉深度监视，强制开启deep
    //   deep: false
    // })

    //  特殊情况
    //  这种情况就需要去开启深度监视
    watch(() => person.job, (newValue, oldValue) => {
      console.log(newValue, oldValue)
    }, {
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