<template>
  <h2>我是About的内容</h2>
</template>

<script>
export default {
  name: "About",
  // mounted() {
  //   console.log('Home组件挂载完毕', this);
  //   window.aboutRoute = this.$route;
  //   window.aboutRouter = this.$router;
  // },
  // beforeDestroy() {
  //   console.log('About组件即将被销毁');
  // }

  // 通过路由规则，进入该组件时被嗲用
  beforeRouteEnter(to,from,next){
    if (to.meta.isAuth){
      if (localStorage.getItem("name") === 'Disney'){
        next()
      }else{
        alert("没有权限")
      }
    }else{
      next()
    }

  },
  // 通过路由规则，离开该组件时被调用
  beforeRouteLeave(to,from,next){
    next()
  }
}
</script>

<style scoped>

</style>