package com.practice.bookstore.security

import com.practice.bookstore.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails(val user: User) : UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities : MutableList<SimpleGrantedAuthority> = user.roles.map{
            SimpleGrantedAuthority(it.authority)
        }.toMutableList()

        return authorities;
    }

    override fun getPassword(): String {
        return user.password;
    }

    override fun getUsername(): String {
        return user.email;
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
        return true;
    }
}