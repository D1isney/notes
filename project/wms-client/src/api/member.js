import request from '@/utils/request'
import {getToken} from "@/utils/auth";


export function getList(param){
  return request({
    url:"/member/list",
    method: 'get',
    data: param
  })
}

//  登录接口
export function login(data) {
  return request({
    url: '/member/login',
    method: 'post',
    data: data
  })
}

export function getInfo() {
  return request({
    url: '/member/getInfo/' + getToken() ,
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/member/logout',
    method: 'get'
  })
}

//  强制登录
export function constraintLogin(data) {
  return request({
    url: '/member/constraintLogin',
    method: 'post',
    data: data
  })
}

//  常量
export const MemberConst = {
  // 激活状态（0：封禁，1：可用，2：已过期）
  status: { // 用户状态
    0: { label: '封禁', value: 0, color: '#ff0000' },
    1: { label: '可用', value: 1, color: '#67c23a' },
    2: { label: '过期', value: 2, color: '#fe8d00' },
  },

  // 在线状态 （1：在线，0：下线）
  online: {
    false: { label: '下线', value: 0, color: '#97a8be' },
    true: { label: '在线', value: 1, color: '#67c23a' }
  }
}
