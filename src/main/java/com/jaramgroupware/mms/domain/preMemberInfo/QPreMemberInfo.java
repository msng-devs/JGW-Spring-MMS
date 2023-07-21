package com.jaramgroupware.mms.domain.preMemberInfo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPreMemberInfo is a Querydsl query type for PreMemberInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPreMemberInfo extends EntityPathBase<PreMemberInfo> {

    private static final long serialVersionUID = 1717337701L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPreMemberInfo preMemberInfo = new QPreMemberInfo("preMemberInfo");

    public final DatePath<java.time.LocalDate> expectedDateReturnSchool = createDate("expectedDateReturnSchool", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.jaramgroupware.mms.domain.major.QMajor major;

    public final StringPath name = createString("name");

    public final com.jaramgroupware.mms.domain.rank.QRank rank;

    public final com.jaramgroupware.mms.domain.registerCode.QRegisterCode registerCode;

    public final com.jaramgroupware.mms.domain.role.QRole role;

    public final StringPath studentId = createString("studentId");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QPreMemberInfo(String variable) {
        this(PreMemberInfo.class, forVariable(variable), INITS);
    }

    public QPreMemberInfo(Path<? extends PreMemberInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPreMemberInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPreMemberInfo(PathMetadata metadata, PathInits inits) {
        this(PreMemberInfo.class, metadata, inits);
    }

    public QPreMemberInfo(Class<? extends PreMemberInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.major = inits.isInitialized("major") ? new com.jaramgroupware.mms.domain.major.QMajor(forProperty("major")) : null;
        this.rank = inits.isInitialized("rank") ? new com.jaramgroupware.mms.domain.rank.QRank(forProperty("rank")) : null;
        this.registerCode = inits.isInitialized("registerCode") ? new com.jaramgroupware.mms.domain.registerCode.QRegisterCode(forProperty("registerCode"), inits.get("registerCode")) : null;
        this.role = inits.isInitialized("role") ? new com.jaramgroupware.mms.domain.role.QRole(forProperty("role")) : null;
    }

}

