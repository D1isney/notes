<template>
  <h1>Vue3</h1>
  <h2>name:{{ name }}</h2>
  <h2>age:{{ age }}</h2>
  <h2>sex:{{ sex }}</h2>
  <button @click="sayHello">+++---</button>
</template>

<script>

// import {h} from "vue";

export default {
  name: 'App',
  data() {
    return {
      sex: '男',
    }
  },
  methods: {
    sayVue() {
      console.log(this.name + " + ", this.sex)
    }
  },
  // async setup() {
  setup() {
    let name = '张三'
    let age = 20

    function sayHello() {
      alert("HelloVue3")
    }

    //  返回一个对象
    return {
      name, age,
      sayHello
    }

    //  渲染函数
    // return () => {
    //   return h('div','123')
    // }
  }
}
</script>

<style>
</style>
