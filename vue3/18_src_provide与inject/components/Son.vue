<template>
  <div class="son">
    <h3>Son组件{{ car }}</h3>
  </div>
</template>
<script>

import {inject} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Son',
  setup() {
    let car = inject("car");
    return {
      car
    }
  }
}
</script>
<style>
.son {
  background-color: orange;
  padding: 10px;
}
</style>