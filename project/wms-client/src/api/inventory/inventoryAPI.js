import request from '@/utils/request'
import { data } from 'autoprefixer'

export function getInventoryListAPI(query) {
  return request({
    url: '/inventory/list',
    method: 'get',
    params: query
  })
}

export function warehousing(data) {
  return request({
    url: '/inventory/warehousing',
    method: 'post',
    data: data
  })
}

export function intelligent() {
  return request({
    url: '/inventory/intelligentDiskLibrary',
    method: 'get'
  })
}

export function getBillOfInventory(){
  return request({
    url: '/inventory/billOfInventory',
    method: 'get'
  })
}

