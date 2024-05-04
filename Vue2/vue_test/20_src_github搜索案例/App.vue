<template>
  <div className="container">
    <Search></Search>
    <List/>
  </div>
</template>

<script>
import Search from "./components/Search";
import List from "./components/List";

export default {
  name: "App",
  components: {
    Search,
    List
  }
}
</script>
<style lang="css">
</style>