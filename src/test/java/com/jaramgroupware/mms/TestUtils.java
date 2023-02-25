package com.jaramgroupware.mms;

import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class TestUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LocalDate testDate;
    private final LocalDate testDate2;
    private final Major testMajor;
    private final Major testMajor2;
    private final Role testRole;
    private final Role testRole2;
    private final Rank testRank;
    private final Rank testRank2;
    private final LocalDateTime testDateTime = LocalDateTime.parse("2022-08-04 04:16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final LocalDateTime testDateTime2 = LocalDateTime.parse("2022-08-28 04:16:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final Member testMember;
    private final Member testMember2;
    private final Member testMember3;
    private final Member testMember4;
    private final MemberInfo testMemberInfo;
    private final MemberInfo testMemberInfo2;
    private final MemberLeaveAbsence testMemberLeaveAbsence;
    private final MemberLeaveAbsence testMemberLeaveAbsence2;
    private final LocalDate testReturnDate;
    private final LocalDate testReturnDate2;
    public final String testUid;

    public boolean isListSame(List<?> targetListA , List<?> targetListB){

        if(targetListA.size() != targetListB.size()) return false;
        for (int i = 0; i < targetListA.size(); i++) {
            try{
                targetListA.indexOf(targetListB.get(i));
            }catch (Exception e){
                logger.debug("{}",targetListA.get(i).toString());
                logger.debug("{}",targetListB.get(i).toString());
                return false;
            }
        }
        return true;
    }
    public TestUtils(){

        testDate = LocalDate.of(2022,1,22);
        testDate2 = LocalDate.of(2022,8,28);
        testReturnDate = LocalDate.of(2024,3,2);
        testReturnDate2 = LocalDate.of(2024,9,1);
        testMajor = Major.builder()
                .id(1)
                .name("인공지능학과")
                .build();
        testMajor2 = Major.builder()
                .id(2)
                .name("소프트웨어학부 컴퓨터 전공")
                .build();
        testRole = Role.builder()
                .id(1)
                .name("ROLE_ADMIN")
                .build();
        testRole2 = Role.builder()
                .id(2)
                .name("ROLE_DEV")
                .build();
        testRank = Rank.builder()
                .id(1)
                .name("정회원")
                .build();
        testRank2 = Rank.builder()
                .id(2)
                .name("준OB")
                .build();
        testMember = Member.builder()
                .id("Th1s1sNotRea1U1DDOY0UKNOWH0S")
                .name("황테스트")
                .email("hwangTest@test.com")
                .role(testRole)
                .status(false)
                .build();
        testMember2 = Member.builder()
                .id("ThiS1SNotRea1U1DDOY0UKNOWHoS")
                .name("김테스트")
                .email("kimTest@test.com")
                .role(testRole2)
                .status(true)
                .build();
        testMember3 = Member.builder()
                .id("AASDFGHJKLZXCVBNMQWERTYUIOPS")
                .name("이테스트")
                .email("lee@test.com")
                .role(testRole)
                .status(false)
                .build();
        testMember4 = Member.builder()
                .id("QWERTYUIOPASDFGHJKLZXCVBNMQQ")
                .name("희테스트")
                .email("hee@test.com")
                .role(testRole2)
                .status(true)
                .build();
        testMemberInfo = MemberInfo.builder()
                .id(1)
                .member(testMember)
                .phoneNumber("01000000000")
                .studentID("2022000004")
                .year(38)
                .rank(testRank)
                .major(testMajor)
                .dateOfBirth(testDate)
                .build();
        testMemberInfo.setModifiedDateTime(testDateTime);
        testMemberInfo.setCreatedDateTime(testDateTime);
        testMemberInfo.setModifiedBy("system");
        testMemberInfo.setCreateBy("system");
        testMemberInfo2 = MemberInfo.builder()
                .id(2)
                .member(testMember2)
                .phoneNumber("01000000011")
                .studentID("2022000005")
                .year(37)
                .rank(testRank2)
                .major(testMajor2)
                .dateOfBirth(testDate2)
                .build();
        testMemberInfo2.setModifiedDateTime(testDateTime2);
        testMemberInfo2.setCreatedDateTime(testDateTime2);
        testMemberInfo2.setModifiedBy("system2");
        testMemberInfo2.setCreateBy("system2");
        testMemberLeaveAbsence = MemberLeaveAbsence.builder()
                .id(1)
                .member(testMember)
                .status(true)
                .expectedDateReturnSchool(testReturnDate)
                .build();
        testMemberLeaveAbsence2 = MemberLeaveAbsence.builder()
                .id(2)
                .member(testMember2)
                .status(true)
                .expectedDateReturnSchool(testReturnDate2)
                .build();
        testUid = testMember.getId();
    }
    public HttpEntity<?> createHttpEntity(Object dto,String userUid){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user_uid",userUid);

        return new HttpEntity<>(dto, headers);
    }
    public Map<String,Object> getString(String arg, Object value) {
        return Collections.singletonMap(arg, value);
    }
}
