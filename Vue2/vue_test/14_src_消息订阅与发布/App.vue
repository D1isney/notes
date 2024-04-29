<template>
  <div>
    <h1>{{ msg }}</h1>
    <student></student>
    <school></school>
  </div>
</template>
<script>
import Student from "./components/Student.vue";
import School from "./components/School.vue";

export default {
  name: 'App',
  components: {School, Student},
  data() {
    return {
      msg: 'Hello Vue',
      StudentName: '',
    }
  },
  methods: {

  },
  mounted() {
  }
}
</script>
<style>

</style>