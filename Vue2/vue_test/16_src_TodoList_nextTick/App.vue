<template>
  <div id="root">
    <div class="todo-container">
      <div class="todo-wrap">
        <Header @addTodo="addTodo"/>
        <List
            :todos="todos"
        />
        <Footer
            :todos="todos"
            @checkAllTodo="checkAllTodo"
            @clearAllDoneTodo="clearAllDoneTodo"
        />
      </div>
    </div>
  </div>
</template>
<script>

import pubsub from "pubsub-js";
import Header from "./components/Header.vue";
import Footer from "./components/Footer.vue";
import List from "./components/List.vue";
export default {

  name: 'App',
  components: {List, Footer, Header},
  data() {
    return {
      // todos: [
      // {id: '001', title: '吃饭', done: false},
      // {id: '002', title: "睡觉", done: true},
      // {id: '003', title: '打代码', done: false}
      // ]
      todos: JSON.parse(localStorage.getItem('todos')) || []
    }
  },
  methods: {
    //添加的todo
    addTodo(todo) {
      console.log('我是app组件，我收到了数据');
      this.todos.unshift(todo);
    },
    checkTodo(id) {
      const todo = this.todos.find(todo => todo.id === id);
      todo.done = !todo.done;
    },
    // 用下划线占位
    deleteTodo(_,id) {
      this.todos = this.todos.filter(todo => todo.id !== id);
    },
    checkAllTodo(done) {
      this.todos.forEach(todo => todo.done = done);
    },
    clearAllDoneTodo() {
      this.todos = this.todos.filter(todo => !todo.done)
    },
    //  更新一个todo
    updateTodo(id,title){
      this.todos.forEach((todo)=>{
        if (todo.id === id) todo.title = title
      })
    }
  },
  watch: {

    todos: {
      deep: true,
      handler(value) {
        localStorage.setItem("todos", JSON.stringify(value))
      }
    }
  },
  // 绑定事件、接收数据的
  mounted() {
    this.$bus.$on('checkTodo',this.checkTodo)
    // 全局事件总线
    // this.$bus.$on('deleteTodo',this.deleteTodo)
    this.pid =pubsub.subscribe('deleteTodo',this.deleteTodo)

    this.$bus.$on('updateTodo',this.updateTodo)
  },
  // 解绑
  beforeDestroy() {
    this.$bus.$off('checkTodo')
    this.$bus.$off('updateTodo')
    // this.$bus.$off('deleteTodo')
    pubsub.unsubscribe(this.pid);
  }
}
</script>
<style>
/*base*/
body {
  background: #fff;
}

.btn {
  display: inline-block;
  padding: 4px 12px;
  margin-bottom: 0;
  font-size: 14px;
  line-height: 20px;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

.btn-danger {
  color: #fff;
  background-color: #da4f49;
  border: 1px solid #bd362f;
}
.btn-edit {
  color: #fff;
  background-color: #7edaee;
  border: 1px solid #297a85;
}

.btn-danger:hover {
  color: #fff;
  background-color: #bd362f;
}

.btn:focus {
  outline: none;
}

.todo-container {
  width: 600px;
  margin: 0 auto;
}

.todo-container .todo-wrap {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

</style>