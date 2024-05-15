<template>
  <div>
    <ul>
      <li v-for="(item,index) in messageList" :key="item.id">
        <!--        跳转路由并携带query参数，to的字符串写法-->
        <!--        <router-link :to="`/home/message/detail?id=${item.id}&title=${item.title}`">{{ item.title }}</router-link>-->

        <!--        跳转路由并携带query参数，to的对象写法-->
        <router-link :to="{
                  name:'detail',
                  query:{
                    id: item.id,
                    title: item.title
                  }
                }">
          {{ item.title }}
        </router-link>

        <button @click="pushShow(item)">push查看</button>
        <button @click="replaceShow(item)">replace查看</button>

        <!--        <router-link :to="`/home/message/detail/${item.id}/${item.title}`">{{ item.title }}</router-link>-->
      </li>
    </ul>
    <hr>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "Message",
  data() {
    return {
      messageList: [
        {id: '001', title: '消息01'},
        {id: '002', title: '消息02'},
        {id: '003', title: '消息03'}
      ]
    }
  },
  methods:{
    pushShow(item){
      this.$router.push({
        name:'detail',
        query:{
          id: item.id,
          title: item.title
        }
      })
    },
    replaceShow(item){
      this.$router.replace({
        name:'detail',
        query:{
          id: item.id,
          title: item.title
        }
      })
    }
  }
}
</script>

<style scoped>

</style>