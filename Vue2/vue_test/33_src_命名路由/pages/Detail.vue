<template>
  <ul>
    <li>
<!--      ID：{{ $route.query.id }}-->
      ID：{{ $route.params.id }}
      <br>
<!--      Title：{{ $route.query.title }}-->
      Title：{{ $route.params.title }}
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