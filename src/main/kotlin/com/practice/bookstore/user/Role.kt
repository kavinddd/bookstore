package com.practice.bookstore.user

import org.springframework.security.core.GrantedAuthority

data class Role(val authorityName: String): GrantedAuthority {

    override fun getAuthority(): String = authorityName;

}