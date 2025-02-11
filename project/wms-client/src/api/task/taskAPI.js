import request from '@/utils/request'

export function getTaskList(query) {
  return request({ url: '/task/list', params: query })
}

export const TaskConst = {
  // 任务状态（0：初始化，1：进行中，2：挂起，3：完成）
  status: { // 用户状态
    0: { label: '初始化', value: 0, color: '#a3c494' },
    1: { label: '进行中', value: 1, color: '#67c23a' },
    2: { label: '挂起', value: 2, color: '#d3bc61' },
    3: { label: '完成', value: 3, color: '#345470' },
    4: { label: '入库', value: 4, color: '#a3c494' },
    5: { label: '出库', value: 5, color: '#67c23a' }
  },
  type: {
    0: { label: '入库', value: 4, color: '#a3c494' },
    1: { label: '出库', value: 5, color: '#67c23a' }
  },
}

