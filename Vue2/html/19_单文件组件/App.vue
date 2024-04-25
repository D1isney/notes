<template>
  <div>
    <School></School>
    <Student></Student>
  </div>
</template>
<script>
import School from './School.vue'
import Student from './Student.vue'
export default {
  name:'App',
  components: {School},
  data(){
    return{}
  },
  methods:{},
  Components:{
    School,
    Student
  }
}
</script>
<style>

</style>