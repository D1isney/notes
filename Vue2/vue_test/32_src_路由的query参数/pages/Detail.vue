<template>
  <ul>
    <li>
      ID：{{ $route.query.id }}
      <br>
      Title：{{ $route.query.title }}
    </li>
  </ul>
</template>
<script>
export default {
  name:'Detail',
  data(){
    return{}
  },
  methods:{},
  components:{},
  mounted() {
    console.log(this.$route)
  }
}
</script>
<style>

</style>