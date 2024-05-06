<template>
  <div class="container">
    <Category title="美食" :listData="foods">
      <img slot="center" src="https://static001.infoq.cn/resource/image/c4/01/c4251ee38c2039602d69cac1d3ab9d01.jpg"
           alt="">
      <a slot="footer" href="http://localhost:8080">更多美食</a>
    </Category>
    <Category title="游戏" :listData="games">
      <ul slot="center">
        <li v-for="(item,index) in games" :key="index">{{ item }}</li>
      </ul>
    </Category>
    <Category title="电影" :listData="films">
      <video controls></video>
      <h4 slot="footer">欢迎前来观看</h4>
      <!--      v-slot:footer，最新的插槽写法，只能写在template上-->
      <!--      <template v-slot:footer></template>-->
    </Category>

    <!--    拿到传过来的数据-->
    <Category>
      <template slot="test" scope="disney">
        {{disney.test}}}
        <ul>
          <li v-for="(item,index) in disney.test" :key="index">{{ item }}</li>
        </ul>
      </template>
    </Category>
    <Category>
      <template slot="test" scope="tests">
        <ol>
          <li v-for="(item,index) in tests.test" :key="index">{{ item }}</li>
        </ol>
      </template>
    </Category>

  </div>
</template>

<script>

import Category from "./components/Category.vue";

export default {
  name: "App",
  data() {
    return {
      foods: ['火锅', '烧烤', '小龙虾', '牛排'],
      games: ['红色警戒', '穿越火线', '劲舞团', '超级玛丽'],
      films: ['当幸福来敲门', '教父', '《拆弹专家》', '《你好，李焕英》']
    }
  },
  components: {
    Category
  }
}
</script>
<style lang="css">
.container {
  display: flex;
  justify-content: space-around;
}
</style>