<template>
  <div>
    <h2 @click="showName">学生名称：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
  </div>
</template>
<script>
//  引入一个混合
import {mixin} from '../mixin'
import {mixin2} from '../mixin'
export default {
  name: 'Student',
  data() {
    return {
      name: 'Vue',
      sex: '男'
    }
  },
  mixins: [mixin,mixin2],
  methods:{},
}
</script>
<style>

</style>