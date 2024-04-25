<template>
  <div>
    <Student></Student>
    <school></school>
  </div>
</template>
<script>
import School from './School.vue'
import Student from './Student.vue'
export default {
  name:'App',
  components: {School,Student},
  data(){
    return{}
  },
  methods:{},

}
</script>
<style>

</style>