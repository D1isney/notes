<template>
  <div>
    <h1>当前求和为：{{ sum }}</h1>
    <h2>当前求和放大十倍为：{{ bigSum }}</h2>
    <h2>Vue：{{ school }}</h2>
    <select v-model.number="n">
      <option :value="1">1</option>
      <option :value="2">2</option>
      <option :value="3">3</option>
    </select>
    <!--    <button @click="JIA(n)">+</button>-->
    <button @click="increment(n)">+</button>
    <!--    <button @click="JIAN(n)">-</button>-->
    <button @click="decrement(n)">-</button>
    <button @click="incrementOdd(n)">当前求和为奇数再加</button>
    <button @click="incrementWait(n)">等一等再加</button>
  </div>
</template>
<script>
import {mapState, mapGetters, mapMutations, mapActions} from 'vuex'

export default {
  name: 'Count',
  data() {
    return {
      n: 1  //  用户选择的数字
    }
  },
  computed: {
    // 借助mapState生成计算属性，从state中读取数据，数组写法
    ...mapState(['sum', 'school']),

    // getters对象写法
    // ...mapGetters({'bigSum':'bigSum'}),
    ...mapGetters(['bigSum']),
  },
  mounted: function () {
    const x = mapState({
      'sum': 'sum',
      school: 'school',
    });
    console.log(x)
  },
  methods: {
    // increment() {
    //   this.$store.dispatch("jia", this.n);
    //   // console.log(this)
    // },
    // decrement() {
    //   // 跳过actions，没有业务逻辑直接跳过actions
    //   this.$store.commit("JIAN", this.n);
    // },
    //      借助mapMutations生成对应的方法，方法中会调用commit去练习mutations（对象写法）
    //     <button @click="increment(n)">+</button>，需要把值传过去
    ...mapMutations({increment: 'JIA', decrement: "JIAN"}),

    //      借助mapMutations生成对应的方法，方法中会调用commit去练习mutations（数组写法）
    //      <button @click="JIA(n)">+</button><button @click="JIAN(n)">-</button>
    // ...mapMutations(['JIA','JIAN']),

    /****************************/

    // incrementOdd() {
    //   // if (this.$store.state.sum % 2) {
    //   //   this.$store.dispatch("jia", this.n);
    //   // }
    //   this.$store.dispatch("jiaOdd", this.n);
    // },
    // incrementWait() {
    //   // setTimeout(() => {
    //   //   this.$store.dispatch("jia", this.n);
    //   // }, 500)
    //   this.$store.dispatch("jiaWait", this.n);
    // },
    // 对象写法
    ...mapActions({incrementOdd: 'jiaOdd', incrementWait: 'jiaWait'})
    //   数组写法 、方法名也要改跟上面一样
    // ...mapActions(['jiaOdd', 'jiaWait'])
  },
  components: {}
}
</script>
<style>
button {
  margin-left: 5px;
}
</style>