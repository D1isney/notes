<template>
  <h2>姓名：{{ person.name }}</h2>
  <h2>年龄：{{ age }}</h2>
  <h2>薪资：{{ person.job.j1.salary }}</h2>
  <button @click="person.name += '~'">修改姓名</button>
  <button @click="person.age ++">增长年龄</button>
</template>
<script>
import {reactive, ref, toRef, toRefs} from "vue";

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

    // 拿到ref对象
    let name1 = toRef(person.name);
    console.log(name1)

    const x = toRefs(person)
    console.log(x)

    return {
      sum, msg,
      person,
      //  这样写响应式就没有了
      name: person.name,
      // 这样写就是响应式的
      age: toRef(person, "age"),
      //  这是新的，改的不是上面的person
      // age:ref(person.age)
      name1,
      x,
      ...toRefs(person)

    }

  }
}
</script>
<style>

</style>