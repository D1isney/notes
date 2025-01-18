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


//  常量
export const ParamConst = {
  // 参数类型 0：塑料板，1：亚克力板
  type: {
    1: { label: '塑料板', value: 1, color: '#ccc' },
    0: { label: '亚克力板', value: 0, color: '#ccc' }
  }
}
