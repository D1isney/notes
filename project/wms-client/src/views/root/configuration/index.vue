<template>
  <div class="app-container">
    <div class="config-all">
      <div class="role-all">
        <el-table
          ref="roleTable"
          :data="roleList"
          tooltip-effect="dark"
          style="width: 100%"
          height="100%"
          @selection-change="handleSelectionChange">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            prop="code"
            label="角色编码">
          </el-table-column>
          <el-table-column
            prop="name"
            label="角色"
            show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </div>
      <div class="permissions-all">
          <el-transfer
            v-loading="permissionsLoading"
            class="permissions-transfer"
            v-model="permissionsByRoleList"
            filterable
            :titles="['全部权限', '用户权限']"
            :button-texts="['取消', '配置']"
            @change="handleChange"
            :format="{
        noChecked: '${total}',
        hasChecked: '${checked}/${total}'
      }"
            :data="permissionsAllList">
            <span slot-scope="{ option }">
              {{ option.name }} - {{ option.path }}
<!--            {{ option }}-->
            </span>
            <el-button type="primary" class="transfer-footer" slot="right-footer">
              配置
            </el-button>
          </el-transfer>
        </div>
    </div>

  </div>
</template>

<script>
import pagination from '@/components/Pagination/index'
import { getRoleListAPI } from '@/api/role/roleAPI'
import { getPermissionsByRoleId, getPermissionsListAPI } from '@/api/permissions/permissionsAPI'
import { onFreeze } from 'core-js/internals/internal-metadata'

export default {
  components: {
    pagination
  },
  data() {
    return {
      permissionsAllList: [],
      permissionsByRoleList: [],
      query: {},
      roleList: [],
      permissionsLoading: false,
      checkId: 0
    }
  },
  computed: {},
  created() {
    this.getRoleList()
  },
  methods: {
    async handleSelectionChange(selection) {
      if (selection.length > 1) {
        this.$refs.roleTable.clearSelection()
        this.$refs.roleTable.toggleRowSelection(selection.pop())
      } else if (selection.length === 1) {
        this.permissionsLoading = true
        setTimeout(()=>{
          this.checkedRow = selection.pop()
          this.permissionsAllList = []
          this.permissionsByRoleList = []
          this.getPermissionsList()
          this.getPermissionsByRoleList(this.checkedRow.id)
          this.permissionsLoading = false
          this.checkId = this.checkedRow.id
        },500)
      } else {
        this.checkedRow = {}
        this.permissionsLoading = true
        setTimeout(()=>{
          this.checkedRow = selection.pop()
          this.permissionsAllList = []
          this.permissionsByRoleList = []
          this.permissionsLoading = false
        },500)
      }
    },
    generateData(list){
      const data = [];
      for (let i = 0; i < list.length; i++) {
        data.push({
          id: list[i].id,
          key: list[i].id,
          label: list[i].name,
          name: list[i].name,
          path: list[i].path,
          disabled: false
        });
      }
      return data;
    },
    generateDataSelection(list){
      const data = [];
      for (let i = 0; i < list.length; i++) {
        data.push(list[i].id);
      }
      return data;
    },
    getRoleList: async function() {
      await getRoleListAPI(this.query).then(res => {
        if (res.code === 200) {
          this.roleList = res.data.list
        }
      })
    },
    handleChange(value, direction, movedKeys) {
      //  右边剩余量
      console.log(value)
      console.log(this.checkId)

      

    },
    async getPermissionsList() {
      await getPermissionsListAPI(this.query).then(res=>{
        if (res.code === 200){
          if (res.data.list !== null){
              const data =  this.generateData(res.data.list)
              this.permissionsAllList = data
            }
        }
      })
    },
    async getPermissionsByRoleList(id){
      await getPermissionsByRoleId(id).then(res=>{
        if (res.code === 200){
          if (res.data != null){
            const data = this.generateDataSelection(res.data)
            this.permissionsByRoleList = data
          }
        }
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

.config-all {
  width: 100%;
  height: 100%;
  /* 隐藏滚动条，但仍可滚动 */
  .el-table__body-wrapper::-webkit-scrollbar {
    display: none; /* 对于Webkit浏览器 */
  }

  .el-table__body-wrapper {
    -ms-overflow-style: none; /* 对于IE和Edge */
    scrollbar-width: none; /* 对于Firefox */
  }
  display: flex;

  .role-all{
    width: 30%;
    height: 100%;
  }
  .permissions-all{
    width: 60%;
    height: 100%;
    margin-left: 8%;
    .permissions-transfer{
      width: 100%; /* 左右两个穿梭框的高度和宽度 */
      height: 100%;
    }
  }
}
/deep/ .el-transfer-panel{
  width: 35%; /* 左右两个穿梭框的高度和宽度 */
  height: calc(100vh - 50px);
}
/deep/ .el-transfer-panel__list.is-filterable {
  height: calc(100vh - 50px);
}

.transfer-footer {
  margin-left: 20px;
  padding: 6px 5px;
}


</style>

