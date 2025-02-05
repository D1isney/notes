package com.wms.helper;

import com.wms.filter.login.Member;
import com.wms.thread.MemberThreadLocal;
import org.springframework.stereotype.Component;

@Component
public class CurrentHelper {
    public Long getCurrentMemberId(){
        return MemberThreadLocal.get().getMember().getId();
    }

    public Member getCurrentMember(){
        return MemberThreadLocal.get().getMember();
    }

}
