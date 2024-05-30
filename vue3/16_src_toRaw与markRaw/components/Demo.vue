<template>
  <h4>当前求和为：{{ sum }}</h4>
  <button @click="sum++">sum++</button>
  <hr>
  <h2>姓名：{{ person.name }}</h2>
  <h2>年龄：{{ age }}</h2>
  <h2>薪资：{{ person.job.j1.salary }}</h2>
  <h2 v-show="person.car">车：{{ person.car }}</h2>
  <button @click="name += '~'">修改姓名</button>
  <button @click="age++">增长年龄</button>
  <button @click="job.j1.salary++">添加薪资</button>
  <button @click="showRawPerson">输出最原始的Person</button>
  <button @click="addCar">添加一辆车</button>
  <button @click="person.car.name += '!'">换车名</button>
  <button @click="person.car.price++">换价格</button>
</template>
<script>
import {markRaw, reactive, ref, toRaw, toRef, toRefs} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let sum = ref(0)
    let msg = ref('Hello')

    // 浅层次的响应式、只考虑第一层的响应式
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

    function showRawPerson() {
      const p = toRaw(person)
      console.log("原始对象：" + p)
    }

    function addCar() {
      let car = {
        name: '奔驰',
        price: 400000
      }
      // person.car = car;
      // 最原始的数据、东西就不会变了
      person.car = markRaw(car);
    }

    return {
      sum, msg,
      person,
      // 这样写响应式就没有了
      name: person.name,
      // 这样写就是响应式的
      age: toRef(person, "age"),
      // 这是新的，改的不是上面的person
      // age:ref(person.age)
      ...toRefs(person),
      showRawPerson,
      addCar
    }

  }
}
</script>
<style>

</style>