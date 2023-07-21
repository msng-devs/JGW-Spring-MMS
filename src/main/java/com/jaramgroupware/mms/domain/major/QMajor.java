package com.jaramgroupware.mms.domain.major;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMajor is a Querydsl query type for Major
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMajor extends EntityPathBase<Major> {

    private static final long serialVersionUID = -703433855L;

    public static final QMajor major = new QMajor("major");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QMajor(String variable) {
        super(Major.class, forVariable(variable));
    }

    public QMajor(Path<? extends Major> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMajor(PathMetadata metadata) {
        super(Major.class, metadata);
    }

}

