<template>
  <div>
    <Demo msg="Hello" @hello="showHelloMsg"></Demo>
  </div>
</template>

<script>
import Demo from './components/Demo.vue'
export default {
  name: 'App',
  setup() {
    function showHelloMsg(){
      console.log('hello')
    }
    return{
      showHelloMsg
    }
  },
  components: {Demo}
}
</script>

<style>
</style>
