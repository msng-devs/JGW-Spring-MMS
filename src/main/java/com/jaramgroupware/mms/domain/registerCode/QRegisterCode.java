package com.jaramgroupware.mms.domain.registerCode;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegisterCode is a Querydsl query type for RegisterCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegisterCode extends EntityPathBase<RegisterCode> {

    private static final long serialVersionUID = 1187365099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegisterCode registerCode = new QRegisterCode("registerCode");

    public final StringPath code = createString("code");

    public final StringPath createBy = createString("createBy");

    public final DatePath<java.time.LocalDate> expiredAt = createDate("expiredAt", java.time.LocalDate.class);

    public final com.jaramgroupware.mms.domain.preMemberInfo.QPreMemberInfo preMemberInfo;

    public QRegisterCode(String variable) {
        this(RegisterCode.class, forVariable(variable), INITS);
    }

    public QRegisterCode(Path<? extends RegisterCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegisterCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegisterCode(PathMetadata metadata, PathInits inits) {
        this(RegisterCode.class, metadata, inits);
    }

    public QRegisterCode(Class<? extends RegisterCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.preMemberInfo = inits.isInitialized("preMemberInfo") ? new com.jaramgroupware.mms.domain.preMemberInfo.QPreMemberInfo(forProperty("preMemberInfo"), inits.get("preMemberInfo")) : null;
    }

}

