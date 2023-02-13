package com.jaramgroupware.mms.domain.memberInfo;

import java.util.List;

public interface MemberInfoCustomRepository {
    void bulkInsert(List<MemberInfo> memberInfos, String who);
    void bulkUpdate(List<MemberInfo> memberInfos, String who);
}
