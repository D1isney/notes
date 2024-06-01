<template>
  <div class="child">
    <h3>Child组件</h3>
    <Son></Son>
  </div>
</template>
<script>

import Son from "./Son.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Child',
  components: {Son},
  setup() {
  }
}
</script>
<style>
.child {
  background-color: skyblue;
  padding: 10px;
}
</style>