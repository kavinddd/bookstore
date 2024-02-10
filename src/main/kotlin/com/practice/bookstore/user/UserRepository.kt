package com.practice.bookstore.user

import org.apache.coyote.http11.Constants.a
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.springframework.stereotype.Repository


@Repository
class UserRepository(private val dslContext: DSLContext) {
    val SCHEMA_NAME: String = "bookstore"

//    return null if not found
    fun findUserByEmail(email: String): User? {
        val records = dslContext
                .select(
                        DSL.field("$SCHEMA_NAME.app_user.email").`as`("email"),
                        DSL.field("$SCHEMA_NAME.app_user.password").`as`("password"),
                        DSL.field("$SCHEMA_NAME.role_group.authority").`as`("role")
                )
                .from(DSL.table("$SCHEMA_NAME.app_user"))
                .join(DSL.table("$SCHEMA_NAME.app_user_role_group"))
                .on(DSL.field("$SCHEMA_NAME.app_user.id").eq(DSL.field("$SCHEMA_NAME.app_user_role_group.user_id")))
                .join(DSL.table("$SCHEMA_NAME.role_group"))
                .on(DSL.field("$SCHEMA_NAME.role_group.id").eq(DSL.field("$SCHEMA_NAME.app_user_role_group.role_id")))
                .where(DSL.field("$SCHEMA_NAME.app_user.email").eq(email))
                .fetch()

        if (records.isEmpty()) return null;

        val email: String = records.first().getValue("email").toString()
        val password: String = records.first().getValue("password").toString()
        val roles: MutableSet<Role> = records.getValues("role").map { Role(it.toString()) }.toMutableSet();
        val theUser = User(
                email = email,
                password = password,
                roles = roles
        );

        return theUser;
    }

    fun isExistByEmail(email: String): Boolean {
        val record = dslContext
                .select()
                .from(DSL.table("$SCHEMA_NAME.app_user"))
                .where(DSL.field("$SCHEMA_NAME.app_user.email").eq(email))
                .fetch()

        return !record.isEmpty()
    }

    fun createUser(email: String, password: String): Unit {

        val record: Record1<Any>? = dslContext
                .insertInto(DSL.table("$SCHEMA_NAME.app_user"),
                        DSL.field("email"),
                        DSL.field("password")
                )
                .values(email, password)
                .returningResult(DSL.field("$SCHEMA_NAME.app_user.id"))
                .fetchOne()

        val userGeneratedId : Int? = record?.getValue(DSL.field("$SCHEMA_NAME.app_user.id", Int::class.java))

        dslContext
                .insertInto(DSL.table("$SCHEMA_NAME.app_user_role_group"),
                        DSL.field("user_id"),
                        DSL.field("role_id")
                        )
                .values(userGeneratedId, RoleType.ADMIN.roleId)
                .execute()

    }

}

private enum class RoleType(val roleId: Int)  {
                         ADMIN(1), STAFF(2), USER(3)
}