<template>
  <div>
    <h1>{{ msg }}</h1>
    <h2>学生姓名：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
    <h2>学生年龄：{{ myAge }}</h2>
    <button @click="updateAge">尝试修改收到的年龄</button>
  </div>
</template>
<script>
export default {
  name: 'Student',
  data() {
    return {
      msg: 'Vue',
      myAge: this.age,
    }
  },
  //  父组件传过来的数据，传进来的数据不推荐去修改，能修改，但是会有警告
  //  优先级更高
  // props:['name','sex','age'], //  简单接收

  // props: {
  //   name: String,
  //   age: Number,
  //   sex: String
  // },  //  定义类型

  // 接收的同时对数据进行类型限制 + 必要性的限制
  props: {
    name: {
      //  name的类型，String
      type: String,
      // name是必要的
      required: true
    },
    age: {
      type: Number,
      required: false,
      //  默认值
      default: 99
    },
    sex: {
      type: String,
      required: true
    }
  },
  //  prop : 属性
  //  props : 一堆属性

  methods: {
    updateAge: function (){
      this.myAge ++
    }
  },
  components: {}
}
</script>
<style>

</style>