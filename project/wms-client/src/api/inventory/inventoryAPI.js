import request from '@/utils/request'
import { data } from 'autoprefixer'

export function getInventoryListAPI(query) {
  return request({
    url: '/inventory/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateInventory(data) {
  return request({
    url: '/inventory/saveOrUpdateInventory',
    method: 'post',
    data: data
  })
}

