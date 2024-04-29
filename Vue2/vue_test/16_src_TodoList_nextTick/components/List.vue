<template>
  <ul class="todo-main">
    <Item
        v-for="todoObj in todos"
        :key="todoObj.id"
        :todo="todoObj"
    />
  </ul>
</template>
<script>
import Item from "./Item.vue";

export default {
  name: 'List',
  data() {
    return {}
  },
  props: ['todos'],
  methods: {},
  components: {Item}
}
</script>
<style scoped>
/*main*/
.todo-main {
  margin-left: 0;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding: 0;
}

.todo-empty {
  height: 40px;
  line-height: 40px;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding-left: 5px;
  margin-top: 10px;
}
</style>