<template>
  <div>
    <button @click="test">btn</button>
  </div>
</template>
<script>
export default {
  name: 'Demo',
  setup(props,context){
    console.log(context)
    console.log('setup'+context.attrs)
    function test(){
      context.emit('hello')
    }
    return{
      test
    }
  },
  beforeCreate(){
    console.log('beforeCreate')
  }
}
</script>
<style>

</style>