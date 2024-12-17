import request from "@/utils/request";

//  登录接口
export function login(data) {
  return request({
    url: '/member/login',
    method: 'post',
    data: data
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
