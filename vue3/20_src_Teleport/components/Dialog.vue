<template>
  <div>
    <button @click="isShow=true">弹窗</button>
    <!--    <div class="dialog" v-if="isShow">-->
    <!--      <h3>弹窗</h3>-->
    <!--      <h4>一些内容</h4>-->
    <!--      <h4>一些内容</h4>-->
    <!--      <h4>一些内容</h4>-->
    <!--      <button @click="isShow=false">关闭弹窗</button>-->
    <!--    </div>-->
    <teleport to="body">
      <div class="mark" v-if="isShow">
        <div class="dialog">
          <h3>弹窗</h3>
          <h4>一些内容</h4>
          <h4>一些内容</h4>
          <h4>一些内容</h4>
          <button @click="isShow=false">关闭弹窗</button>
        </div>
      </div>
    </teleport>
  </div>
</template>
<script>

import {ref} from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Dialog',
  setup() {
    let isShow = ref(false)
    return {
      isShow
    }
  }
}
</script>
<style>
.dialog {
  width: 300px;
  height: 300px;
  background-color: green;

  position: absolute;
  top: 50%;
  left: 50%;
  text-align: center;
  transform: translate(-50%, -50%);
}

.mark {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
}
</style>