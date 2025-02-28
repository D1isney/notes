<template>
  <div id="app" class="app">
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
  mounted() {
  }
}
</script>
<style scoped>
.app {
  overflow: auto;
  scrollbar-width: none;
  width: 100vw;
  height: 100vh;
}
.app::-webkit-scrollbar {
  display: none;
}

.input[aria-hidden="true"]{
  display: none !important;
}

</style>
