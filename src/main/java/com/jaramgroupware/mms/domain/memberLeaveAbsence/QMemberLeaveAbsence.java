package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberLeaveAbsence is a Querydsl query type for MemberLeaveAbsence
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberLeaveAbsence extends EntityPathBase<MemberLeaveAbsence> {

    private static final long serialVersionUID = -125037333L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberLeaveAbsence memberLeaveAbsence = new QMemberLeaveAbsence("memberLeaveAbsence");

    public final DatePath<java.time.LocalDate> expectedDateReturnSchool = createDate("expectedDateReturnSchool", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.jaramgroupware.mms.domain.member.QMember member;

    public final BooleanPath status = createBoolean("status");

    public QMemberLeaveAbsence(String variable) {
        this(MemberLeaveAbsence.class, forVariable(variable), INITS);
    }

    public QMemberLeaveAbsence(Path<? extends MemberLeaveAbsence> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberLeaveAbsence(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberLeaveAbsence(PathMetadata metadata, PathInits inits) {
        this(MemberLeaveAbsence.class, metadata, inits);
    }

    public QMemberLeaveAbsence(Class<? extends MemberLeaveAbsence> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.jaramgroupware.mms.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

