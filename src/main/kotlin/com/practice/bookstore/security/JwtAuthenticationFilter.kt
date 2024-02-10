package com.practice.bookstore.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val jwtGenerator: JwtGenerator,
        private val customUserDetailsService: UserDetailsService
        ) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        val token: String? = extractTokenFromJwt(request)

        if (token.isNullOrEmpty()) {
            return
        }

        val username: String = extractUsernameFromJwt(request)
        val userDetails: UserDetails = customUserDetailsService.loadUserByUsername(username)
        val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
                )
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
        jwtGenerator.validateToken(token)

        filterChain.doFilter(request, response)

    }

    private fun extractUsernameFromJwt(request: HttpServletRequest): String {
        TODO("Not yet implemented")
    }

    private fun extractTokenFromJwt(request: HttpServletRequest): String? {

        // expected: "Bearer: rAnD0mT0k3N"
        val token: String = request.getHeader("Authorization")

        println("Token $token")

        val isTokenExist: Boolean = token.isNullOrEmpty()
        val isBearerToken: Boolean = token.startsWith("Bearer")

        if (isTokenExist && isBearerToken) {
            println(token.substring(7, token.length))
            return token.substring(7, token.length)
        }

        return null
    }


}