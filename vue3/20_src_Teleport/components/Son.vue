<template>
  <div class="son">
    <h3>Son组件</h3>
    <Dialog></Dialog>
  </div>
</template>
<script>


import Dialog from "./Dialog.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Son',
  components: {Dialog},
  setup() {
  }
}
</script>
<style>
.son {
  background-color: orange;
  padding: 10px;
}
</style>