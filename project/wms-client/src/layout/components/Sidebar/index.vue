<template>
  <div :class="{'has-logo':showLogo}" class="box">
    <logo v-if="showLogo" :collapse="isCollapse"/>

    <el-scrollbar wrap-style="overflow-y:hidden;">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'sidebar',
      'permissions'
    ]),
    routes() {
      const allRoutes = this.$router.options.routes
      const fileRouter = [] // 筛选后的路由
      allRoutes.forEach(route => {
        if (route.meta && Array.isArray(route.meta.permission)) {
          const hasPermission = route.meta.permission.some(permission => this.permissions.includes(permission))
          if (hasPermission) {
            fileRouter.push(route)
          }
        } else {
          fileRouter.push(route)
        }
      })
      return fileRouter
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style scoped>
.el-scrollbar__body {
  overflow: auto;
}

.el-scrollbar__container ::-webkit-scrollbar {
  display: none;
}

/deep/ .el-scrollbar__bar is-horizontal {
  display: none !important;
}

</style>
