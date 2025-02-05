import request from '@/utils/request'

export function getList(query) {
  return request({ url: '/goods/list', params: query })
}

export function getGoodsParamByGoodId(id) {
  return request({
    url: `/goods/getGoodsParamByGoodId/${id}`,
    method: "get"
  })
}


export function getGoodsParamByTypeAndGoodId(type,goodId){
  return request({
    url: `/paramKey/getParamKeyListByType/${type}/${goodId}`,
    method: "get"
  })
}
export function getGoodsParamByType(type){
  return request({
    url: `/paramKey/getParamKeyListByType/${type}`,
    method: "get"
  })
}

export function saveOrUpdateGoods(data){
  return request({
    url: `/goods/saveOrUpdateGoods`,
    method: "post",
    data: data
  })
}

export function deleteGoods(ids) {
  return request({
    url: `/goods/delete?ids=${ids}`,
    method: 'delete'
  })
}
