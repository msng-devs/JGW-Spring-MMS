package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import java.util.List;

public interface MemberLeaveAbsenceCustomRepository {
    void bulkInsert(List<MemberLeaveAbsence> memberLeaveAbsences);
}
