import { login, logout, getInfo } from '@/api/member/member'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    member: {},
    permissions:[]
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_MEMBER: (state, member) => {
    state.member = member
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_PERMISSIONS(state, permissions){
    state.permissions = permissions
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo: function({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        if (response.code === 701 || response.code === 401) {
          removeToken() // must remove  token  first
          commit('RESET_STATE')
        }
        if (data) {
          commit('SET_NAME', data.member.name)
          commit('SET_MEMBER', data.member)
          commit('SET_PERMISSIONS',data.permissions)
          // const accessRoutes = constantRoutes.concat(baseRouter)
          // console.log(accessRoutes)
          // router.addRoutes([
          //   {
          //     path: '/task',
          //     component: Layout,
          //     children: [
          //       {
          //         path: '/task',
          //         name: 'Task',
          //         component: () => import('@/views/task/index'),
          //         meta: { title: '任务管理', icon: 'el-icon-notebook-2', permission: ['task:list'] }
          //       }
          //     ]
          //   }
          // ])
          resolve(data)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

