<template>
  <div>
    <h1>当前求和为：{{ $store.state.sum }}</h1>
    <h2>当前求和放大十倍为：{{ $store.getters.bigSum }}</h2>
    <select v-model.number="n">
      <option :value="1">1</option>
      <option :value="2">2</option>
      <option :value="3">3</option>
    </select>
    <button @click="increment">+</button>
    <button @click="decrement">-</button>
    <button @click="incrementOdd">当前求和为奇数再加</button>
    <button @click="incrementWait">等一等再加</button>
  </div>
</template>
<script>
export default {
  name: 'Count',
  data() {
    return {
      n: 1  //  用户选择的数字
    }
  },
  methods: {
    increment() {
      this.$store.dispatch("jia", this.n);
      // console.log(this)
    },
    decrement() {
      // 跳过actions，没有业务逻辑直接跳过actions
      this.$store.commit("JIAN", this.n);
    },
    incrementOdd() {
      // if (this.$store.state.sum % 2) {
      //   this.$store.dispatch("jia", this.n);
      // }
      this.$store.dispatch("jiaOdd", this.n);
    },
    incrementWait() {
      // setTimeout(() => {
      //   this.$store.dispatch("jia", this.n);
      // }, 500)
      this.$store.dispatch("jiaWait", this.n);
    },
  },
  components: {}
}
</script>
<style>
button {
  margin-left: 5px;
}
</style>