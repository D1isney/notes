<template>
  <div class="app-container">
    <div class="param-table">
      <el-table
        ref="paramTable"
        :data="list"
        :highlight-current-row="true"
        height="93%"
        stripe="stripe"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column type="expand">
          <template slot-scope="propsList">
            <el-form label-position="left" :inline="false" class="demo-table-expand">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="创建时间：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.createTime }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新时间：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.updateTime }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建人：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.createUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="更新人：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.updateUsername }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="描述：">
                    <el-tag
                      type="info"
                      effect="plain"
                      size="small"
                    >
                      <span>{{ propsList.row.remark }}</span>
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>

            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="Key"
          prop="key"
        />
        <el-table-column
          label="参数名"
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
          align="right"
        >
          <template slot="header" slot-scope="scope">
            <el-row>
              <el-col :span="7">
                <el-input v-model="query.name" clearable placeholder="参数名"></el-input>
              </el-col>
              <el-col :span="7" :push="1">
                <el-select v-model="query.type" placeholder="板材类型">
                  <el-option
                    v-for="item in optionsType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="7">
                <el-button type="primary" icon="el-icon-search" @click="getList"/>
              </el-col>
            </el-row>
          </template>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click="openEditDrawer(scope.row)"/>
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
      title="修改参数"
      :visible.sync="editDrawer"
      :direction="direction"
    >
      <el-form ref="editParamList" :model="editParamList" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="Key：">
              <el-input v-model="editParamList.key"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="参数名：">
              <el-input v-model="editParamList.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="板材类型：">
              <el-select v-model="defaultSelect" placeholder="板材类型">
                <el-option
                  v-for="item in optionsType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16" :push="2">
            <el-form-item label="描述：">
              <el-input type="input" v-model="editParamList.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="updateEditParamList">buttonCont</el-button>
      </el-form>

    </el-drawer>
  </div>
</template>

<script>

import { getParamsList, ParamConst } from '@/api/params/paramsAPI'
import pagination from '@/components/Pagination/index.vue'
import { MemberConst } from '@/api/member/member'

export default {
  components: {
    pagination
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        type: '',
        name: ''
      },
      total: 10,
      list: [],
      optionsType: [],
      direction: 'rtl',
      editDrawer: false,
      editParamList: {},
      defaultSelect:0
    }
  },
  computed: {
    typeOptions: () => Object.keys(ParamConst.type).map(key => ParamConst.type[key])
  },
  created() {
    this.getList()
    //  多选
    this.optionsType = ParamConst.type
    console.log(this.optionsType)
  },
  methods: {
    async getList() {
      await getParamsList(this.query).then(res => {
        if (res.code === 200) {
          this.list = res.data.list
          this.total = res.data.totalCount
        }
      })
    },

    typeTag(online) {
      if (online > 0) {
        return 'success'
      } else {
        return 'info'
      }
    },

    openEditDrawer(row) {
      this.editDrawer = true
      this.defaultSelect = this.typeOptions[row.type].value
      this.editParamList = row
    },
    updateEditParamList(){
      this.editParamList.type = this.defaultSelect
      console.log(this.editParamList)
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

