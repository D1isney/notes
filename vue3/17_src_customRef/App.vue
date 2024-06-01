<template>
  <div>
    <input type="text" v-model="keyWord">
    <h3>{{ keyWord }}</h3>
  </div>
</template>

<script>

import {customRef} from "vue";

export default {
  name: 'App',
  setup() {
    //  自定义一个ref 名为myRef
    function myRef(value,deley) {
      let timer;
      return customRef((track, trigger) => {
        return {
          get: function () {
            //  通知Vue去追踪Value的变化（提前和get商量一下，让他认为这个value是有用的）
            track()
            return value
          },
          set: function (newValue) {
            clearTimeout(timer)
            timer = setTimeout(() => {
              // 通知vue去重新解析模版
              value = newValue
              trigger()
            }, deley)
          }
        }
      })
    }

    let keyWord = myRef('hello',500)

    //  使用自定义的ref


    return {
      keyWord
    }
  },
  components: {}
}
</script>

<style>
</style>
