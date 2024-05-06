<template>
  <div class="category">
    <h3>{{ title }}分类</h3>
    <!--    <ul v-show="title !== '美食' ">-->
    <!--      <li v-for="(item,index) in listData" :key="index">{{ item }}</li>-->
    <!--    </ul>-->
    <!--    <img v-show=" title === '美食' " src="https://static001.infoq.cn/resource/image/c4/01/c4251ee38c2039602d69cac1d3ab9d01.jpg" alt="">-->
    <!--     <video></video>-->

    <!--    插槽、定一个插槽（等着组件的使用者进行填充）-->
    <!--    默认插槽-->
    <slot name="center">如果没有使用，就会出现插槽的默认值</slot>
    <slot name="footer">如果没有使用，就会出现插槽的默认值</slot>

    <!--    把tests传一个数据-->
    <slot :test="tests" name="test">如果没有使用，就会出现插槽的默认值</slot>

  </div>
</template>
<script>
export default {
  name: 'Category',
  data() {
    return {
      tests: [
        '1', '2', '3'
      ]
    }
  },
  methods: {},
  components: {},
  props: ['title']
}
</script>
<style scoped>
.category {
  background-color: skyblue;
  width: 200px;
  height: 300px;
}

img {
  width: 100%;
}

video {
  width: 100%;
}

h3 {
  text-align: center;
  background-color: orange;
}
</style>