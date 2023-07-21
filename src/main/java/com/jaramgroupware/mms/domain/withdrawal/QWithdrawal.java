package com.jaramgroupware.mms.domain.withdrawal;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWithdrawal is a Querydsl query type for Withdrawal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdrawal extends EntityPathBase<Withdrawal> {

    private static final long serialVersionUID = 230216139L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWithdrawal withdrawal = new QWithdrawal("withdrawal");

    public final DatePath<java.time.LocalDate> createDate = createDate("createDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.jaramgroupware.mms.domain.member.QMember member;

    public final DatePath<java.time.LocalDate> withdrawalDate = createDate("withdrawalDate", java.time.LocalDate.class);

    public QWithdrawal(String variable) {
        this(Withdrawal.class, forVariable(variable), INITS);
    }

    public QWithdrawal(Path<? extends Withdrawal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWithdrawal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWithdrawal(PathMetadata metadata, PathInits inits) {
        this(Withdrawal.class, metadata, inits);
    }

    public QWithdrawal(Class<? extends Withdrawal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.jaramgroupware.mms.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

