import request from '@/utils/request'
import { data } from 'autoprefixer'

export function getTaskList(query) {
  return request({
    url: '/task/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateTask(data){
  return request({
    url:'/task/saveOrUpdateTask',
    method: 'post',
    data: data
  })
}
export function manualOperationIssued(data){
  return request({
    url: '/task/manualOperationIssued',
    method: 'post',
    data: data
  })
}
export function getGoodsAndInventory(data){
  return request({
    url: '/task/getGoodsAndInventory',
    method: 'post',
    data: data
  })
}

export function deleteTask(ids){
  return request({
    url: `/task/delete?ids=${ids}`,
    method: 'delete'
  })
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

