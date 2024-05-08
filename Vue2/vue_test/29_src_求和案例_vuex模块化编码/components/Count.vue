<template>
  <div>
    <h1>当前求和为：{{ sum }}</h1>
    <h2>当前求和放大十倍为：{{ bigSum }}</h2>
    <h2>Vue：{{ school }}</h2>
    <h3>总人数：{{ personList.length }}</h3>
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
    ...mapState('countAbout', ['sum', 'school']),
    ...mapState('personAbout', ['personList']),

    // getters对象写法
    // ...mapGetters({'bigSum':'bigSum'}),
    ...mapGetters('countAbout', ['bigSum']),
  },
  mounted: function () {
    const x = mapState({
      'sum': 'sum',
      school: 'school',
    });
    console.log(x)
  },
  methods: {

    // 需要说明使用哪个模块
    ...mapMutations('countAbout', {increment: 'JIA', decrement: "JIAN"}),

    ...mapActions('countAbout', {incrementOdd: 'jiaOdd', incrementWait: 'jiaWait'})

  },
  components: {}
}
</script>
<style>
button {
  margin-left: 5px;
}
</style>