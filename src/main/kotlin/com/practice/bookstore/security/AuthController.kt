package com.practice.bookstore.security

import com.practice.bookstore.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.EntityResponse

@RestController
@RequestMapping("/auth")
class AuthController(
        val authManager: AuthenticationManager,
        val userDetailsService: UserDetailsService,
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder,
        val jwtGenerator: JwtGenerator
        ){

    @GetMapping("roles")
    fun checkRoles(authentication: Authentication): String {
        val roles = authentication.authorities.map{it.authority}
        return "Authenticated user roles: $roles"
    }

    @PostMapping("register")
    fun registerUser(@RequestBody registerUserDTO: RegisterUserDTO): ResponseEntity<String> {

        val isEmailDuplicated: Boolean = userRepository.isExistByEmail(registerUserDTO.email)

        if (isEmailDuplicated) {
            return ResponseEntity<String>(
                    "Email was taken",
                    HttpStatus.BAD_REQUEST
            )
        }

        val encodedPassword: String = passwordEncoder.encode(registerUserDTO.password)

        userRepository.createUser(registerUserDTO.email, encodedPassword)

        return ResponseEntity<String>(
                "User creation success",
                HttpStatus.OK
        )
    }

    @PostMapping("login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<AuthResponseDTO> {
        val authentication: Authentication = authManager.authenticate(
                UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password)
        )

        println("SecurityContextHolder Before: ${SecurityContextHolder.getContext()}")
        SecurityContextHolder.getContext().authentication = authentication
        println("SecurityContextHolder After: ${SecurityContextHolder.getContext()}")
        println(SecurityContextHolder.getContext().authentication)

        val token: String = jwtGenerator.generateToken()

        return ResponseEntity<AuthResponseDTO>(
                "Login Success",
                HttpStatus.OK
        )
    }

}