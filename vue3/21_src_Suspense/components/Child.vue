<template>
  <div class="child">
    <h3>Child组件</h3>
    {{ sum }}
  </div>
</template>
<script>


import {ref} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Child',
  async setup() {
    let sum = ref(0)
    let p = new Promise((resolve, reject) => {
      console.log(reject)
      setTimeout(() => {
        resolve({sum})
      }, 1000)
    })
    return await p
    // return{
    //   sum
    // }
  }
}
</script>
<style>
.child {
  background-color: skyblue;
  padding: 10px;
}
</style>