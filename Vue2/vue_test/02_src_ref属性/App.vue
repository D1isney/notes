<template>
  <div>
    <h2 id="h2" ref="h2">{{msg}}</h2>
    <School id="sch" ref="sch"></School>
    <Student ref="st" id="st"></Student>
    <button @click="showDOM">点我输出上方的DOM元素</button>
  </div>
</template>
<script>
import Student from "./components/Student.vue";
import School from "./components/School.vue";
export default {
  name:'App',
  data(){
    return{
      msg:'Hello Vue'
    }
  },
  methods:{
    showDOM:function (){
      console.log(document.getElementById("h2"))
      console.log(this.$refs.h2)
      console.log("==============")
      console.log(this.$refs.st);
      console.log(document.getElementById("st"));
      console.log("==============")
      console.log(this.$refs.sch)
      console.log(document.getElementById("sch"));
    //     //  id拿
    //   console.log("真实DOM对象："+document.getElementById('title'));
    //   //  ref拿
    //   console.log("真实DOM对象："+this.$refs.title);
    //   console.log("vc实例对象："+this.$refs)
    //   console.log("vc实例对象的真实DOM对象："+document.getElementById("sch"));
    }
  },
  components:{Student,School}
}
</script>
<style>

</style>