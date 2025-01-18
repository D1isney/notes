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
