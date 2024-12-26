import request from '@/utils/request'

export function getSettingAPI() {
  return request({
    url: '/system/getSetting',
    method: 'get'
  })
}

export function saveOrUpdateSettingAPI(data) {
  return request({
    url: '/system/saveOrUpdateSetting',
    method: 'post',
    data: data
  })
}

export function openPlcConnect() {
  return request({
    url: '/system/open',
    method: 'get'
  })
}
export function closePlcConnect() {
  return request({
    url: '/system/close',
    method: 'get'
  })
}
export function getSystemPlcStatus() {
  return request({
    url: '/system/plcStatus',
    method: 'get'
  })
}

