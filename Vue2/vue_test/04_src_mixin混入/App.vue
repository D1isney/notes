<template>
  <div>
    <Student></Student>
    <School></School>
  </div>
</template>
<script>
import Student from "./components/Student.vue";
import School from "./components/School.vue";

export default {
  name:'App',
  components: {School, Student},
  data(){
    return{
      msg:'Hello Vue'
    }
  },
  methods:{

  },
}
</script>
<style>

</style>