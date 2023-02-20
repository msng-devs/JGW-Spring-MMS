package com.jaramgroupware.mms.domain.member;



import java.util.List;

public interface MemberCustomRepository {
    void bulkInsert(List<Member> members);
    void bulkUpdate(List<Member> members);
}
