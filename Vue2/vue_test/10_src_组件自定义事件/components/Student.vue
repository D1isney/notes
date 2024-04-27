<template>
  <div class="student">
    <h2>学生名称：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
    <button @click="sendStudentName">把学生名称给APP</button>
    <button @click="unbind">解绑disney事件</button>
    <button @click="death">销毁当前Student组件的实例对象(VC)</button>
  </div>
</template>
<script>
export default {
  name: 'Student',
  data() {
    return {
      name: 'Vue',
      sex: '男'
    }
  },
  methods: {
    sendStudentName() {
      //  发射、触发的意思
      //   触发Student组件实例身上的事件
      this.$emit('disney', this.name)
      this.$emit('demo')
    },
    unbind() {
      //  解绑一个自适应事件
      // this.$off('disney')
      //  解绑多个
      // this.$off (['disney','demo'])
      //  接绑所有自定义事件
      this.$off();
    },
    death() {
      //  销毁后，所有Student实例自定义事件全都被解绑
      this.$destroy();
    },
    mounted() {
      // 给Student绑定的事件 this是自己，所有要自己触发
      this.$on('test', (data) => {
        console.log("test事件被触发了", data)
      })
    }
  },
}
</script>
<style lang="less" scoped>
.student {
  background-color: orangered;
}
</style>