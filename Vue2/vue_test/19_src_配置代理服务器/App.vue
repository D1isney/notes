<template>
  <div>
    <button @click="getStudent">
      获取学生信息
    </button>
    <button @click="getCar">
      获取汽车的信息
    </button>
  </div>
</template>
<script>

//  引入axios
import axios from "axios";

export default {
  name: 'App',
  components: {},
  data() {
    return {}
  },
  methods: {
    getStudent() {
      // 开启了代理服务器，所以使用自己本来的服务器8080
      // 如果自己的根目录有这个东西，就不会通过代理区访问后端了
      axios.get("http://localhost:8080/api/UserManage/user/getALLUser").then(
          response => {
            console.log("成功了",response.data)
          },
          error => {
            console.log("请求失败了",error.message)
          }
      )
    },
    getCar(){
      axios.get("http://localhost:8080/api/UserManage/user/getALLUser").then(
          response => {
            console.log("成功了",response.data)
          },
          error => {
            console.log("请求失败了",error.message)
          }
      )
    }
  }
}
</script>
<style>
</style>