import request from '@/utils/request'

export function getList(query) {
  return request({ url: '/log/list', params: query })
}

export const LogConst = {
  // 激活状态（0：封禁，1：可用，2：已过期）
  type: { // 用户状态
    0: { label: '普通', value: 0, color: '#67c23a' },
    1: { label: '警告', value: 1, color: '#fe8d00' },
    2: { label: '危险', value: 2, color: '#FE4C00' },
    3: { label: '报警', value: 3, color: '#ff0000' },
    4: { label: '入库', value: 4, color: '#3dbb5a' },
    5: { label: '出库', value: 5, color: '#2a8bbe' },
  }
}
