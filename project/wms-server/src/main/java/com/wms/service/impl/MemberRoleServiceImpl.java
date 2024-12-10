package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.MemberDao;
import com.wms.dao.MemberRoleDao;
import com.wms.pojo.MemberRole;
import com.wms.service.MemberRoleService;
import org.springframework.stereotype.Service;

@Service
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleDao, MemberRole> implements MemberRoleService {
}
