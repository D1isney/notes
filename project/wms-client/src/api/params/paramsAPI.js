import request from '@/utils/request'

export function getParamsList(query) {
  return request({ url: '/paramKey/list', params: query })
}
