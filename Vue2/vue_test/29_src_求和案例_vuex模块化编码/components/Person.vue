<template>
  <div>
    <h1>求和：{{ sum }}</h1>
    <h1>人员列表</h1>
    <h2>列表中第一个人的名字是：{{ firstPersonName }}</h2>
    <input type="text" placeholder="请输入名字" v-model="name">
    <button @click="add">添加</button>
    <button @click="addWang">添加一个姓王的人</button>
    <ul>
      <li v-for="p in personList" :key="p.id">
        {{ p.name }}
      </li>
    </ul>
  </div>
</template>
<script>
import {mapState} from "vuex";
import {nanoid} from "nanoid";

export default {
  name: 'Person',
  data() {
    return {
      name: ''
    }
  },
  methods: {
    add() {
      let personObj = {id: nanoid(), name: this.name}
      this.$store.commit('personAbout/ADD_PERSON', personObj)
      this.name = '';
    },
    addWang(){
      let personObj = {id: nanoid(), name: this.name}
      this.$store.dispatch('personAbout/addPersonWang', personObj)
      this.name = '';
    }
  },
  components: {},
  computed: {
    personList() {
      return this.$store.state.personAbout.personList;
    },
    // ...mapState(['personList']),
    sum(){
      return this.$store.state.countAbout.sum;
    },
    firstPersonName(){
      return this.$store.getters["personAbout/firstPersonName"];
    }
  }
}
</script>
<style>

</style>