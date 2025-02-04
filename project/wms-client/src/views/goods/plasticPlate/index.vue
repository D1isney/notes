<template>
  <div class="app-container">
    <div class="acrylicPlate-table">
      <el-table
        ref="acrylicPlateTable"
        :data="list"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        row-key="id"
        :expand-row-keys="expands"
        @row-click="openExpand"
        @expand-change="expandChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column type="expand">
          <template slot-scope="propsList">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-row :gutter="20" v-if="propsList.row.params.length">
                <el-col :span="12" v-for="item in propsList.row.params">
                  <el-form-item :label="item.text">
                    <span>{{ item.value }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="物料编码"
          prop="code"
        />
        <el-table-column
          label="物料名"
          prop="name"
        />
        <el-table-column
          label="类型"
        >
          <template v-slot="{row}">
            <el-tag
              :type="typeTag(row.type)"
              effect="dark"
              size="small"
            >
              {{ (row.type || row.type === 0) && typeOptions[row.type].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          prop="createTime"
        />
        <el-table-column
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-input
                  v-model="query.param"
                  placeholder="查询"
                  clearable
                />
              </el-col>
              <el-col :span="4" :push="2">
                <el-button type="primary" icon="el-icon-search" @click="getList()"/>
              </el-col>
              <el-col :span="4" :push="2">
                <el-button type="primary" icon="el-icon-refresh" @click="clear()"/>
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click.stop="openEditDrawer(scope.row)"/>
            <el-button type="danger" icon="el-icon-delete" circle/>
          </template>
        </el-table-column>
      </el-table>
      <div class="button-box">
        <el-button type="primary" class="button-box-add" icon="el-icon-plus"/>
        <el-button type="danger" class="button-box-delete" icon="el-icon-delete-solid"/>
      </div>
    </div>
    <div class="page">
      <pagination :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList"/>
    </div>

    <el-drawer
      title="物料修改"
      :visible.sync="editDrawer"
      :direction="direction"
    >
      <el-form ref="editGoodsList" :model="editGoodsList" label-width="100px">
        <el-form-item label="物料编码">
          <el-col :span="15">
            <el-input v-model="editGoodsList.code" disabled></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="参数名">
          <el-col :span="15">
            <el-input v-model="editGoodsList.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="板材类型">
          <el-col :span="15">
            <el-select v-model="defaultSelect" placeholder="板材类型">
              <el-option
                v-for="item in optionsType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-row :gutter="10"
                v-for="param in editGoodsList.params"
        >
          <el-form-item :label="param.text" key="param.id"
          >
            <el-col :span="15">
              <el-input :placeholder="param.text" v-model="param.value"/>
            </el-col>
            <el-col :span="1" :push="1">
              <el-button type="danger" icon="el-icon-circle-close" circle @click="deleteParam(param)"/>
            </el-col>
          </el-form-item>
        </el-row>

        <!--            <el-tag-->
        <!--              @close="deleteParam(param)"-->
        <!--              closable-->
        <!--              :disable-transitions="false">-->
        <!--              {{ param.text }}-->
        <!--            </el-tag>-->
        <!--            <el-input type="text" />-->
        <!--          <el-col :span="13" :push="3">-->
        <!--          </el-col>-->
        <el-form-item>
          <el-col :span="15">

            <el-button type="primary" @click="editGoods">修改</el-button>
            <el-button type="info" @click="editDrawer = false">取消</el-button>

            <el-dropdown split-button type="primary" icon="el-icon-circle-plus-outline"
                         @click="addParamDefault()"
            >
              添加参数
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in editGoodsParam" @click.native="addParam(item)">
                  {{ item.name }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>
<script>

import { getGoodsParamByGoodId, getGoodsParamByType, getList } from '@/api/goods/acrylicPlateAPI'
import pagination from '@/components/Pagination/index.vue'
import { ParamConst } from '@/api/params/paramsAPI'
import item from '@/layout/components/Sidebar/Item.vue'
import * as buffer from 'buffer'

export default {
  components: {
    pagination
  },
  data() {
    return {
      getRowKeys(row) {
        return row.id
      },
      editDrawer: false,
      direction: 'rtl',
      query: {
        page: 1,
        limit: 10,
        type: 0,
        param: ''
      },
      total: 10,
      list: [],
      totalCount: 0,
      expands: [],
      optionsType: [],
      defaultSelect: 0,
      editGoodsList: {},
      editGoodsParam: []
    }
  },
  computed: {
    item() {
      return item
    },
    typeOptions: () => Object.keys(ParamConst.type).map(key => ParamConst.type[key])
  },
  created() {
    this.getList()
    this.optionsType = ParamConst.type
  },
  methods: {
    addParamDefault(){
      if (this.editGoodsParam.length > 0){
        this.addParam(this.editGoodsParam[0])
      }
    },
    addParam(val) {
      const { goodId = this.editGoodsList.id, paramId = val.id, text = val.key, name } = val
      const data = {
        goodId,
        name,
        paramId,
        text,
        value: ''
      }
      this.editGoodsParam = this.editGoodsParam.filter(item => {
        const id = item.id ?? item.paramId
        return id !== paramId
      })
      this.editGoodsList.params.push(data)
    },
    deleteParam(param) {
      let index = this.editGoodsList.params.findIndex((ele) => {
        return ele.paramId === param.paramId
      })
      if (index !== -1) {
        this.editGoodsList.params.splice(index, 1)
      }
      this.editGoodsParam.push(param)
    },
    openEditDrawer(row) {
      this.editDrawer = true
      this.defaultSelect = this.typeOptions[row.type].value
      this.editGoodsList = JSON.parse(JSON.stringify(row))
      getGoodsParamByType(this.editGoodsList.type, this.editGoodsList.id).then(res => {
        this.editGoodsParam = []
        if (res.code === 200) {
          this.editGoodsParam = res.data
        }
      })
    },
    async clear() {
      this.query = {
        page: 1,
        limit: 10,
        type: 0,
        param: ''
      }
      await getList(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
          for (let i = 0; i < this.list.length; i++) {
            getGoodsParamByGoodId(this.list[i].id).then(request => {
              if (request.code === 200) {
                this.$set(this.list[i], 'params', request.data)
              }
            })
          }
        }
      })
    },
    async getList() {
      await getList(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
          for (let i = 0; i < this.list.length; i++) {
            getGoodsParamByGoodId(this.list[i].id).then(request => {
              if (request.code === 200) {
                this.$set(this.list[i], 'params', request.data)
              }
            })
          }
        }
      })
    },
    typeTag(type) {
      if (type > 0) {
        return 'success'
      } else {
        return 'info'
      }
    },
    //  小箭头
    openExpand: function(row, colum, event) {
      if (this.expands.includes(row.id)) {
        this.expands = this.expands.filter(value => value !== row.id)
      } else {
        this.expands.push(row.id)
      }
    },
    //  多个小箭头
    expandChange(row, expandedRows) {//
      let that = this
      if (expandedRows.length) {//此时展开
        that.expands = []
        if (row) {
          that.expands.push(row.id)
        }
      } else {//折叠
        that.expands = []
      }
    },
    editGoods(){
      console.log(this.editGoodsList)
    }
  }
}
</script>
<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
}

.acrylicPlate-table {
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

.button-box {
  width: 100%;
  height: 7%;
  display: flex;
  justify-content: flex-end;
  padding-right: 0.5%;

  .button-box-add {
    width: 4%;
    height: 80%;
  }

  .button-box-delete {
    width: 4%;
    height: 80%;
  }
}

.page {
  width: 100%;
  height: 30px;
  line-height: 30px;
  float: right;
}
</style>

