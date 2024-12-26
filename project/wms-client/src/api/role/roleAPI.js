import request from '@/utils/request'

export function getRoleListAPI(query) {
  return request({
    url: '/role/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateRoleAPI(data){
  return request({
    url: '/role/saveOrUpdate',
    method: 'post',
    data: data
  })
}

export function deleteRoleApi(ids){
  return request({
    url: `/role/delete?ids=${ids}`,
    method: 'delete'
  })
}
export function getPermissionsByRoleId(id){
  return request({
    url: `/role/getPermissionsByRoleId/${id}`,
    method: 'get'
  })
}


export const RoleConst = {
  // 角色状态（0：不可用角色，1：可用角色）
  status: { // 用户状态
    0: { label: '不可用角色', value: 0 },
    1: { label: '可用角色', value: 1 }
  }
}
