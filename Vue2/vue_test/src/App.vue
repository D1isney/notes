<template>
  <div>
    <Student></Student>
    <School></School>
  </div>
</template>
<script>
import School from './components/School'
import Student from './components/Student'

export default {
  name: 'App',
  components: {
    School: School,
    Student: Student
  },

}
</script>
<style>

</style>