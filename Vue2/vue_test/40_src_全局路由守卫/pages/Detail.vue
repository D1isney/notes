<template>
  <ul>
    <li>
<!--      ID：{{ $route.query.id }}-->
<!--      ID：{{ a }}-->
      ID：{{ id }}
      <br>
<!--      Title：{{ $route.query.title }}-->
      Title：{{ title }}
    </li>
  </ul>
</template>
<script>
export default {
  name: 'Detail',
  data() {
    return {}
  },
  methods: {},
  components: {},
  mounted() {
    // console.log(this.$route)
  },
  props: ['id', 'title']
}
</script>
<style>

</style>