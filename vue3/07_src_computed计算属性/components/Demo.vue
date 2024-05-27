<template>
  <h1>个人信息</h1>
  姓：<input type="text" v-model="person.firstName">
  <br>
  名：<input type="text" v-model="person.lastName">
  <br>
  <span>全名：{{ fullName }}</span>
  <br>
  全名：<input type="text" v-model="fullName">
</template>
<script>
import {reactive, computed} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Demo',
  setup() {
    let person = reactive({
      firstName: "张",
      lastName: "三"
    })
    // 计算属性，简写（没有修改的情况）
    // let fullName = computed(() => {
    //   return person.firstName + "-" + person.lastName;
    // })
    // 计算属性，简写（没有修改的情况）
    let fullName = computed({
      get() {
        return person.firstName + "-" + person.lastName;
      },
      set(value) {
        let name = value.split("-");
        person.firstName = name[0];
        person.lastName = name[1];
      }
    })

    return {
      person, fullName
    }
  }
}
</script>
<style>

</style>