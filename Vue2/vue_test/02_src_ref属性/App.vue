<template>
  <div>
    <h1 v-text="msg" id="title" ref="title"></h1>
    <button @click="showDOM" ref="btn">点我输出上方的DOM元素</button>
    <School ref="sch" id="sch"></School>
  </div>
</template>
<script>
import School from "./components/Schoole.vue";
export default {
  name:'App',
  data(){
    return{
      msg:'Hello Vue'
    }
  },
  methods:{
    showDOM(){
      //  id拿
      console.log("真实DOM对象："+document.getElementById('title'));
      //  ref拿
      console.log("真实DOM对象："+this.$refs.title);
      console.log("vc实例对象："+this.$refs)
      console.log("vc实例对象的真实DOM对象："+document.getElementById("sch"));
    }
  },
  components:{School}
}
</script>
<style>

</style>