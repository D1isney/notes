<template>
  <div class="student">
    <h2>学生名称：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
  </div>
</template>
<script>
//  引入pubsub
import pubsub from 'pubsub-js'

export default {
  name: 'Student',
  data() {
    return {
      name: 'Vue',
      sex: '男'
    }
  },
  methods: {},
  mounted() {
    // console.log(this.x)
    // this.$bus.$on('hello', (data) => {
    //   console.log("接收到了数据：" + data)
    // })
    this.pubid = pubsub.subscribe("hello", function (name, data){
      console.log(this)//undefined  写成箭头函数 或者吧方法抽出来，放在methods上，this就是vc
      console.log("有人发布了Hello消息成功", name)
      console.log(data)
    })
  },
  beforeDestroy() {
    // // 销毁前 解绑 不要占用
    // this.$bus.$off('hello')

    //   销毁
    //  取消订阅、有点想定时器
    pubsub.unsubscribe(this.pubid)
  }
}
</script>
<style lang="less" scoped>
.student {
  background-color: orangered;
}
</style>