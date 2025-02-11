import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/storage',
    component: Layout,
    children: [
      {
        path: '/storage',
        name: 'Storage',
        component: () => import('@/views/storage/index'),
        meta: { title: '库位信息', icon: 'el-icon-s-grid' }
      }
    ]
  },
  {
    path: '/task',
    component: Layout,
    children: [
      {
        path: '/task',
        name: 'Task',
        component: () => import('@/views/task/index'),
        meta: { title: '任务管理', icon: 'el-icon-notebook-2' }
      }
    ]
  },
  {
    path: '/goods',
    component: Layout,
    redirect: '/goods/acrylicPlate',
    name: 'Goods',
    meta: {
      title: '物料管理',
      icon: 'el-icon-takeaway-box'
    },
    children: [
      {
        path: 'acrylicPlate',
        name: 'AcrylicPlate',
        component: () => import('@/views/goods/acrylicPlate/index'),
        meta: { title: '亚克力板', icon: 'el-icon-minus' }
      },
      {
        path: 'plasticPlate',
        name: 'PlasticPlate',
        component: () => import('@/views/goods/plasticPlate/index'),
        meta: { title: '塑料板', icon: 'el-icon-minus' }
      },
    ]
  },
  {
    path: '/param',
    component: Layout,
    children: [
      {
        path: '/param',
        name: 'Param',
        component: () => import('@/views/param/index'),
        meta: { title: '参数管理', icon: 'el-icon-c-scale-to-original' }
      }
    ]
  },
  {
    path: '/log',
    component: Layout,
    children: [
      {
        path: '/log',
        name: 'Log',
        component: () => import('@/views/log/index'),
        meta: { title: '日志管理', icon: 'el-icon-date' }
      }
    ]
  },
  {
    path: '/root',
    component: Layout,
    redirect: '/root/role',
    name: 'Root',
    meta: { title: '用户管理', icon: 'el-icon-set-up' },
    children: [
      {
        path: 'member',
        name: 'Member',
        component: () => import('@/views/root/member/index'),
        meta: { title: '用户', icon: 'el-icon-s-custom' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/root/role/index'),
        meta: { title: '角色', icon: 'el-icon-s-check' }
      },
      {
        path: 'permissions',
        name: 'Permissions',
        component: () => import('@/views/root/permissions/index'),
        meta: { title: '权限', icon: 'tree' }
      },
      {
        path: 'configuration',
        name: 'Configuration',
        component: () => import('@/views/root/configuration/index'),
        meta: { title: '权限配置', icon: 'el-icon-s-operation' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
