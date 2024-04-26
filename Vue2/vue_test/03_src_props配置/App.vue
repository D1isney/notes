<template>
  <div>
    <Student name="李四" sex="女"></Student>
    <Student name="王五" sex="男" :age="29+1"></Student>
  </div>
</template>
<script>
import Student from "./components/Student.vue";
export default {
  name:'App',
  data(){
    return{
      msg:'Hello Vue'
    }
  },
  methods:{

  },
  components:{Student}
}
</script>
<style>

</style>