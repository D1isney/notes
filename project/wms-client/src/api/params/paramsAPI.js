import request from '@/utils/request'

export function getParamsList(query) {
  return request({ url: '/paramKey/list', params: query })
}
export function deleteParam(ids) {
  return request({
    url: `/paramKey/delete?ids=${ids}`,
    method: 'delete'
  })
}

export function paramKeySaveOrUpdate(data) {
  return request({
    url: `/paramKey/saveOrUpdate`,
    method: "post",
    data: data
  })
}


//  常量
export const ParamConst = {
  // 参数类型 0：塑料板，1：亚克力板
  type: {
    0: { label: '塑料板', value: 0, color: '#ccc' },
    1: { label: '亚克力板', value: 1, color: '#ccc' }
  }
}
