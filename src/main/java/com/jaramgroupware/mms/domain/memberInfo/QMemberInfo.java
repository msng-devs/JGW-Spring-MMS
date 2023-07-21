package com.jaramgroupware.mms.domain.memberInfo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberInfo is a Querydsl query type for MemberInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberInfo extends EntityPathBase<MemberInfo> {

    private static final long serialVersionUID = 2051003371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberInfo memberInfo = new QMemberInfo("memberInfo");

    public final com.jaramgroupware.mms.domain.QBaseEntity _super = new com.jaramgroupware.mms.domain.QBaseEntity(this);

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final DatePath<java.time.LocalDate> dateOfBirth = createDate("dateOfBirth", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.jaramgroupware.mms.domain.major.QMajor major;

    public final com.jaramgroupware.mms.domain.member.QMember member;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath phoneNumber = createString("phoneNumber");

    public final com.jaramgroupware.mms.domain.rank.QRank rank;

    public final StringPath studentID = createString("studentID");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QMemberInfo(String variable) {
        this(MemberInfo.class, forVariable(variable), INITS);
    }

    public QMemberInfo(Path<? extends MemberInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberInfo(PathMetadata metadata, PathInits inits) {
        this(MemberInfo.class, metadata, inits);
    }

    public QMemberInfo(Class<? extends MemberInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.major = inits.isInitialized("major") ? new com.jaramgroupware.mms.domain.major.QMajor(forProperty("major")) : null;
        this.member = inits.isInitialized("member") ? new com.jaramgroupware.mms.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
        this.rank = inits.isInitialized("rank") ? new com.jaramgroupware.mms.domain.rank.QRank(forProperty("rank")) : null;
    }

}

