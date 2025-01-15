import request from '@/utils/request'

export function getRemarkAPI() {
  return request({
    url: '/permissions/getRemark',
    method: 'get'
  })
}

export function getPermissionsListAPI(query) {
  return request({
    url: '/permissions/list',
    method: 'get',
    params: query
  })
}

export function saveOrUpdateAPI(data) {
  return request({
    url: '/permissions/saveOrUpdate',
    method: 'post',
    data: data
  })
}

export function deletePermissionsAPI(ids) {
  return request({
    url: `/permissions/delete?ids=${ids}`,
    method: 'delete'
  })
}

export function restSuperPermissions(){
  return request({
    url: `/permissions/restPermissions`,
    method: 'get'
  })
}

export function getPermissionsByRoleId(id){
  return request({
    url: `/permissions/getPermissionsByRoleId/${id}`,
    method: 'get'
  })
}
