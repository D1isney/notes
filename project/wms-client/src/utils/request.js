import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

const service = axios.create({
  baseURL: 'http://localhost:1217',
  timeout: 30 * 1000
})

service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['Authorization'] = getToken()
      config.headers['Content-Type'] = 'application/json;charset=utf-8'
    }
    return config
  },
  error => {
    Message({
      type: 'error',
      message: error,
      duration: 5 * 1000
    })
    console.log(error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 0 || res.code === 400 || res.code === 200) {
      return res
    } else if (res.code === 300) {
      Message({
        type: 'error',
        message: res.message + '系统即将自动退出！',
        duration: 3 * 1000
      })
      setTimeout(() => {
        store.dispatch('user/loOut').then(() => {
          location.reload()
        })
      }, 3000)
      return res
    } else {
      Message({
        type: 'error',
        message: res.message,
        duration: 5 * 1000
      })
      console.log(res.message)
      return Promise.reject(res.message)
    }
  },
  error => {
    if (error.message === 'Network Error') {
      error = '网络连接失败，请稍后再试！'
    }
    Message({
      type: 'error',
      message: error,
      duration: 5 * 1000
    })
    console.log(error)
    return Promise.reject(error)
  }
)

export default service
