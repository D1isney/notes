import axios from 'axios'
import { Message } from 'element-ui'
import { getToken, removeToken } from '@/utils/auth'
import store from '@/store'
import router from '@/router'
import { BASEURL } from '@/settings'

const service = axios.create({
  baseURL: BASEURL,
  timeout: 1000
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
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if ([0, 200, 701].includes(res.code)) {
      return res
    } else if ([400].includes(res.code)) { // Token失效
      store.dispatch('user/resetToken').then(() => {
        Message.error(res.message)
        setTimeout(()=>{
          router.push('/login')
        },2*1000)
      })
    // } else if ([401].includes(res.code)) { // 登录失效
    //   store.dispatch('user/resetToken').then(r => {
    //     setTimeout(() => {
    //       router.push('/login')
    //     },1 * 1000)
    //   })
    //   return res
    } else {
      console.log('响应错误:', response.config.url, res.code, res.message)
      Message.error(res.message)
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
