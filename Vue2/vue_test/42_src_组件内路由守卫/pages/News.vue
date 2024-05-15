<template>
  <ul>
    <li :style="{opacity}">欢迎学习Vue</li>
    <li>news001 <input type="text"></li>
    <li>news002 <input type="text"></li>
    <li>news003 <input type="text"></li>
  </ul>
</template>

<script>
export default {
  name: "News",
  data(){
    return {
      opacity: 1
    }
  },
  // mounted() {
  //   setInterval(() => {
  //     this.opacity -= 0.001
  //     if (this.opacity <= 0) this.opacity = 11
  //
  //   })
  // }

  // 激活
  activated() {
    console.log("News被激活了")
    this.timer =  setInterval(() => {
      this.opacity -= 0.001
      if (this.opacity <= 0) this.opacity = 11

    })
  },
  // 失活
  deactivated() {
    console.log("News被失活了")
    clearInterval(this.timer)
  }
}
</script>

<style scoped>

</style>