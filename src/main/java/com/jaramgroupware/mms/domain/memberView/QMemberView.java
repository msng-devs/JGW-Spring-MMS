package com.jaramgroupware.mms.domain.memberView;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberView is a Querydsl query type for MemberView
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberView extends EntityPathBase<MemberView> {

    private static final long serialVersionUID = -266386549L;

    public static final QMemberView memberView = new QMemberView("memberView");

    public final StringPath cellPhoneNumber = createString("cellPhoneNumber");

    public final DatePath<java.time.LocalDate> dateOfBirth = createDate("dateOfBirth", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final BooleanPath isLeaveAbsence = createBoolean("isLeaveAbsence");

    public final NumberPath<Long> major = createNumber("major", Long.class);

    public final StringPath majorName = createString("majorName");

    public final StringPath name = createString("name");

    public final NumberPath<Long> rank = createNumber("rank", Long.class);

    public final StringPath rankName = createString("rankName");

    public final NumberPath<Long> role = createNumber("role", Long.class);

    public final StringPath roleName = createString("roleName");

    public final BooleanPath status = createBoolean("status");

    public final StringPath studentId = createString("studentId");

    public final StringPath uid = createString("uid");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QMemberView(String variable) {
        super(MemberView.class, forVariable(variable));
    }

    public QMemberView(Path<? extends MemberView> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberView(PathMetadata metadata) {
        super(MemberView.class, metadata);
    }

}

