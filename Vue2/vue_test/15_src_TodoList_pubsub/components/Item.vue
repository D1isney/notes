<template>
  <li>
    <label>
      <!--      这里勾选和取消勾选可以使用change和click作为事件处理-->
      <input type="checkbox" :checked="todo.done" @change="handleCheck(todo.id)"/>
      <!--v-model数据的双向绑定，checkbox使用v-model来双向绑定其是否被勾选,也可以实现效果但不推荐(因为其实修改了props中的数据)-->
      <!--这里修改了从List修改过来的props,这里的不允许改是浅层次，就是如果props是一个对象则这个修改这个对象的某一个属性vue是放行的-->
      <!-- <input type="checkbox" v-model="todo.done"/>-->
      <span v-show="!todo.isEdit">{{ todo.title }}</span>
      <input v-show="todo.isEdit" type="text" :value="todo.title" @blur="handleBlur(todo,$event)">
    </label>
    <button class="btn btn-danger" @click="handleDelete(todo.id)">删除</button>
    <button class="btn btn-edit" v-show="!todo.isEdit" style="margin-right: 5px" @click="handleEdit(todo)">编辑</button>
  </li>
</template>
<script>
import pubsub from "pubsub-js";

export default {
  name: 'Item',
  //声明接收todo
  props: ['todo'],
  data() {
    return {}
  },
  methods: {
    handleCheck(id) {
      // this.checkTodo(id);
      // 触发事件、发数据
      this.$bus.$emit('checkTodo', id)
    },
    handleDelete(id) {
      if (confirm(`确定删除编号为${id}的todo吗`)) {
        // console.log(id);
        // this.deleteTodo(id);
        //   触发事件、发数据
        // this.$bus.$emit('deleteTodo',id)

        pubsub.publish('deleteTodo', id)
      }
    },
    handleEdit(todo) {
      //  判断有没有这个属性
      if (todo.hasOwnProperty('isEdit')) {
        todo.isEdit = true
      } else {
        this.$set(todo, 'isEdit', true)
      }
      console.log(todo)
    },
    // 失去焦点回调
    handleBlur(todo, e) {
      todo.isEdit = false

      if(!e.target.value.trim()){
        return  alert("输入不能为空")
      }
      this.$bus.$emit('updateTodo', todo.id, e.target.value)
    }

  },
  components: {}
}
</script>
<style scoped>
/*item*/
li {
  list-style: none;
  height: 36px;
  line-height: 36px;
  padding: 0 5px;
  border-bottom: 1px solid #ddd;
}

li label {
  float: left;
  cursor: pointer;
}

li label li input {
  vertical-align: middle;
  margin-right: 6px;
  position: relative;
  top: -1px;
}

li button {
  float: right;
  display: none;
  margin-top: 3px;
}

li:before {
  content: initial;
}

li:last-child {
  border-bottom: none;
}

li:hover {
  background: #ddd;
}

li:hover button {
  display: block;
}
</style>