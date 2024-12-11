package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.MemberDao;
import com.wms.dao.MemberRoleDao;
import com.wms.pojo.MemberRole;
import com.wms.service.MemberRoleService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.MemberRoleVo;
import org.springframework.stereotype.Service;

@Service
public class MemberRoleServiceImpl extends IBaseServiceImpl<MemberRoleDao, MemberRole, MemberRoleVo> implements MemberRoleService {
    @Override
    public MemberRole insertOrUpdate(MemberRole memberRole) {
        return null;
    }
}
