<template>
  <div class="app-container">
    <div class="param-table">

    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList" />
    </div>
  </div>
</template>

<script>

import { getParamsList } from '@/api/params/paramsAPI'
import pagination from '@/components/Pagination/index.vue'

export default {
  components: {
    pagination
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        username: '',
        status: '',
        online: ''
      },
      total: 10
    }
  },
  computed: {
  },
  created() {
    this.getList()
  },
  methods: {
    async getList(){
      await getParamsList(this.query).then(res=>{
        console.log(res)
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
}

.param-table {
  width: 100%;
  height: calc(100% - 30px);

  /* 隐藏滚动条，但仍可滚动 */
  .el-table__body-wrapper::-webkit-scrollbar {
    display: none; /* 对于Webkit浏览器 */
  }

  .el-table__body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }
}

.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
}
</style>

