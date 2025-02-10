import request from '@/utils/request'

export function getStorageListAPI(query) {
  return request({
    url: '/storage/list',
    method: 'get',
    params: query
  })
}

export function getStorageListAndGetInventoryList(){
  return request({
    url: '/storage/queryStorageAndInventory',
    method: 'get'
  })
}

export function queryInventoryNum(){
  return request({
    url: '/storage/queryInventoryNum',
    method: 'get'
  })
}
export function saveOrUpdateStorage(data){
  return request({
    url: '/storage/saveOrUpdate',
    method: 'post',
    data: data
  })
}
