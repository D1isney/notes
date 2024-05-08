<template>
  <div>
    <h1>求和：{{ sum }}</h1>
    <h1>人员列表</h1>
    <input type="text" placeholder="请输入名字" v-model="name">
    <button @click="add">添加</button>
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
      this.$store.commit('ADD_PERSON', personObj)
      this.name = '';
    }
  },
  components: {},
  computed: {
    personList() {
      return this.$store.state.personList;
    },
    // ...mapState(['personList']),
    sum(){
      return this.$store.state.sum;
    }
  }
}
</script>
<style>

</style>