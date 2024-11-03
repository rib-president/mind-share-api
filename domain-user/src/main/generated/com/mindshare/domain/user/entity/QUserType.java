package com.mindshare.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserType is a Querydsl query type for UserType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserType extends EntityPathBase<UserType> {

    private static final long serialVersionUID = -121467927L;

    public static final QUserType userType = new QUserType("userType");

    public final StringPath label = createString("label");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> userTypeId = createNumber("userTypeId", Integer.class);

    public QUserType(String variable) {
        super(UserType.class, forVariable(variable));
    }

    public QUserType(Path<? extends UserType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserType(PathMetadata metadata) {
        super(UserType.class, metadata);
    }

}

