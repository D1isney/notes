package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.MemberDao;
import com.wms.pojo.Member;
import com.wms.service.MemberService;
import org.springframework.stereotype.Service;
@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao,Member> implements MemberService {
}
