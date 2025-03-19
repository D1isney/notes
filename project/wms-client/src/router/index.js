import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import store from '@/store'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
    meta: {permission:['/']}
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true,
    meta: {permission:['/']}
  },

  {
    path: '/',
    component: Layout,
    redirect: '/storage',
    meta: {permission:['/']},
    children: [
      {
        path: '/storage',
        name: 'Storage',
        component: () => import('@/views/storage/index'),
        meta: { title: '首页', icon: 'el-icon-s-grid', permission: ['/'] }
      }
    ]
  },

  {
    path: '/dashboard',
    component: Layout,
    meta: {permission:['/']},
    children: [{
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '数据看板', icon: 'dashboard' , permission: ['/']}
    }]
  },
  {
    path: '/task',
    component: Layout,
    meta: {permission: ["task:list"]},
    children: [
      {
        path: '/task',
        name: 'Task',
        component: () => import('@/views/task/index'),
        meta: { title: '任务管理', icon: 'el-icon-notebook-2', permission: ['task:list'] }
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
      icon: 'el-icon-takeaway-box',
      permission: ['goods:list']
    },
    children: [
      {
        path: 'acrylicPlate',
        name: 'AcrylicPlate',
        component: () => import('@/views/goods/acrylicPlate/index'),
        meta: { title: '亚克力板', icon: 'el-icon-minus', permission: ['goods:list'] }
      },
      {
        path: 'plasticPlate',
        name: 'PlasticPlate',
        component: () => import('@/views/goods/plasticPlate/index'),
        meta: { title: '塑料板', icon: 'el-icon-minus', permission: ['goods:list'] }
      }
    ]
  },
  {
    path: '/param',
    component: Layout,
    meta: {permission: ['paramKey:list']},
    children: [
      {
        path: '/param',
        name: 'Param',
        component: () => import('@/views/param/index'),
        meta: { title: '参数管理', icon: 'el-icon-c-scale-to-original', permission: ['paramKey:list'] }
      }
    ]
  },
  {
    path: '/log',
    component: Layout,
    meta: {permission: ['log:list']},
    children: [
      {
        path: '/log',
        name: 'Log',
        component: () => import('@/views/log/index'),
        meta: { title: '日志管理', icon: 'el-icon-date', permission: ['log:list'] }
      }
    ]
  },
  {
    path: '/root',
    component: Layout,
    redirect: '/root/role',
    name: 'Root',
    meta: {
      title: '用户管理',
      icon: 'el-icon-set-up',
      permission: ['member:list', 'role:list', 'permissions:list']
    },
    children: [
      {
        path: 'member',
        name: 'Member',
        component: () => import('@/views/root/member/index'),
        meta: { title: '用户', icon: 'el-icon-s-custom', permission: ['member:list'] }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/root/role/index'),
        meta: { title: '角色', icon: 'el-icon-s-check', permission: ['role:list'] }
      },
      {
        path: 'permissions',
        name: 'Permissions',
        component: () => import('@/views/root/permissions/index'),
        meta: { title: '权限', icon: 'tree', permission: ['permissions:list'] }
      },
      {
        path: 'configuration',
        name: 'Configuration',
        component: () => import('@/views/root/configuration/index'),
        meta: { title: '权限配置', icon: 'el-icon-s-operation', permission: ['permissions:saveOrUpdate'] }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true ,meta: {permission: ['/']}}
]

// 需要权限的路由在这里配
export const baseRouter = [
  {
    path: '/task',
    component: Layout,
    children: [
      {
        path: '/task',
        name: 'Task',
        component: () => import('@/views/task/index'),
        meta: { title: '任务管理', icon: 'el-icon-notebook-2', permission: ['task:list'] }
      }
    ]
  },
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

//
function permissionRouter(router = constantRoutes) {
  return router.filter(item => {
    if (item.children) {
      item.children = permissionRouter(item.children)
    }
    return item.meta.permission.includes('permissions:saveOrUpdate')
  })
}

const router = createRouter()

// const router = permissionRouter()
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}



export default router
