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
  }
}
</script>

<style scoped>

</style>