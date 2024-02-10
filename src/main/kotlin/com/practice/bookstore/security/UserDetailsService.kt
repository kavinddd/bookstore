package com.practice.bookstore.security

import com.practice.bookstore.user.User
import com.practice.bookstore.user.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsService(private val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        username!!
        val user: User = userRepository.findUserByEmail(username) ?: throw UsernameNotFoundException("Username $username is not found");
        return UserDetails(user);
    }


}