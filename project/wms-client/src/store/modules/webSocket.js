import { getToken } from '@/utils/auth'
// import {Message, MessageBox} from 'element-ui'
// import store from '@/store'
import settings from '@/settings'

// websocket url
const WEB_SOCKET_URL = process.env.NODE_ENV === 'development' ? '127.0.0.1:1217' : `${window.location.host}`
// const WEB_SOCKET_URL = settings.webSocketURL

// 心跳机制
const heartCheck = {
  timeout: 10000,
  timer: null,
  start: function() {
    this.timer = setTimeout(function() {
      if (state.isConnect) state.socket.send('WebSocket 好久没来消息啦~~~')
    }, this.timeout)
  },
  reset: function() {
    clearTimeout(this.timer)
    this.start()
  },
  clear: function() {
    clearTimeout(this.timer)
    this.timer = null
  }
}

const getDefaultState = () => {
  return {
    socket: '', // WebSocket
    socketData: '', // 接收到的数据
    isConnect: false, // 标记是否连接成功
    reConnectCount: 0, // 断开或没有连接成功，计数重连次数
    reConnectTimer: null // 重连定时器
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_SOCKET_DATA(state, data) {
    if (data) state.socketData = data
  }
}

const actions = {
  async openSocket({ commit, dispatch, state }) {
    // console.log('openSocket')
    const token = getToken()

    // 退出登录了，关闭webSocket连接
    if (!token) {
      console.log('退出登录了，关闭webSocket连接')
      await dispatch('closeSocket')
      return
    }
    try {
      let url = `ws://${WEB_SOCKET_URL}/websocket/${token}`
      state.socket = new WebSocket(url)
      state.socket.onopen = () => {
        console.log('socket.onopen websocket连接成功')
        state.isConnect = true
        state.reConnectCount = 0
        heartCheck.start()
      }
      state.socket.onmessage = e => {
        // console.log('socket.onmessage')
        if (e.data.indexOf('code') != -1) {
          let serveData = JSON.parse(e.data)
          if (serveData.code === 0) {
            commit('SET_SOCKET_DATA', serveData)
          }
        }
        heartCheck.reset()
      }
      state.socket.onclose = () => {
        // console.log('socket.onclose')
        state.isConnect = false
        dispatch('reConnect')
      }
      state.socket.onerror = () => {
        // console.log('socket.onerror')
        state.isConnect = false
        dispatch('reConnect')
      }
    } catch (e) {
      console.log('尝试创建websocket连接失败')
      dispatch('reConnect')
    }
  },
  closeSocket({ commit, state }) {
    // console.log('closeSocket')
    return new Promise(resolve => {
      state.socket && state.socket.close()
      state.reConnectTimer && clearTimeout(state.reConnectTimer)
      heartCheck.clear()
      commit('RESET_STATE')
      resolve(state)
    })
  },
  reConnect({ dispatch, state }) {
    state.reConnectCount += 1
    console.log('reConnect 尝试重连：', state.reConnectCount)
    // 重连次数过大，很有可能后端关闭了服务，提示
    if (state.reConnectCount % 10 === 0) {
      // Message.error('WebSocket连接失败，请检查下程序是否关闭了')
      console.log('WebSocket连接失败，请检查下服务器是否关闭了')
    }
    if (state.isConnect) return
    state.reConnectTimer && clearTimeout(state.reConnectTimer)
    state.reConnectTimer = setTimeout(() => {
      dispatch('openSocket')
    }, 5000)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
