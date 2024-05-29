<template>
  <div>
    <h2>Test组件</h2>
    <h2>当前点击时鼠标的坐标为：x：{{ point.x }}，y：{{ point.y }}</h2>
  </div>
</template>
<script>
import usePoint from "../hooks/usePoint";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Test',
  setup() {
    let point = usePoint()
    return {
      point
    }
  }
}
</script>
<style>

</style>