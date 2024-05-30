<template>
  <h4>当前求和为：{{ sum }}</h4>
  <button @click="sum++">sum++</button>
  <hr>
  <h2>姓名：{{ person.name }}</h2>
  <h2>年龄：{{ age }}</h2>
  <h2>薪资：{{ person.job.j1.salary }}</h2>
  <h2>x：{{ x }}</h2>
  <button @click="name += '~'">修改姓名</button>
  <button @click="age++">增长年龄</button>
  <button @click="x++">x++</button>
</template>
<script>
import {ref, toRef, toRefs, shallowReactive, shallowRef, readonly, shallowReadonly} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)
    let msg = ref('Hello')

    // 浅层次的响应式、只考虑第一层的响应式
    let person = shallowReactive({
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

    // 深只读
    // person = readonly(person)

    //  浅只读，j1以及salary可以改，第一层不能改
    person = shallowReadonly(person)
    sum = readonly(sum)
    // sum = shallowReadonly(sum)

    //  只能使用在基本数据类型上面、不然没有响应式效果
    let x = shallowRef(0)
    return {
      sum, msg,
      person,
      // 这样写响应式就没有了
      name: person.name,
      // 这样写就是响应式的
      age: toRef(person, "age"),
      // 这是新的，改的不是上面的person
      // age:ref(person.age)
      ...toRefs(person)
      , x
    }

  }
}
</script>
<style>

</style>