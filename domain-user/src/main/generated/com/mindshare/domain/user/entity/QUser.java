package com.mindshare.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 161163279L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.sql.Timestamp> alarmAgreedDatetime = createDateTime("alarmAgreedDatetime", java.sql.Timestamp.class);

    public final DatePath<java.sql.Date> birthDate = createDate("birthDate", java.sql.Date.class);

    public final DateTimePath<java.sql.Timestamp> createdDatetime = createDateTime("createdDatetime", java.sql.Timestamp.class);

    public final StringPath emailAddress = createString("emailAddress");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final DateTimePath<java.sql.Timestamp> updatedDatetime = createDateTime("updatedDatetime", java.sql.Timestamp.class);

    public final ListPath<UserCategory, QUserCategory> userCategories = this.<UserCategory, QUserCategory>createList("userCategories", UserCategory.class, QUserCategory.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigInteger> userId = createNumber("userId", java.math.BigInteger.class);

    public final QUserType userType;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userType = inits.isInitialized("userType") ? new QUserType(forProperty("userType")) : null;
    }

}

