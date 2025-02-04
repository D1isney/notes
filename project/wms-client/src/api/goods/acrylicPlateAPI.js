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


export function getGoodsParamByType(type,goodId){
  return request({
    url: `/paramKey/getParamKeyListByType/${type}/${goodId}`,
    method: "get"
  })
}
