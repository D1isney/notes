import request from '@/utils/request'

export function getInventoryListAPI(query) {
  return request({
    url: '/inventory/list',
    method: 'get',
    params: query
  })
}

