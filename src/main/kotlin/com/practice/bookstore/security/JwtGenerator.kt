package com.practice.bookstore.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultJwtBuilder
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtGenerator {

    val JWT_EXPIRATION: Long = 70000;
    val JWT_SECRET: String = "I love banana"

    fun generateToken(authentication: Authentication): String {
        val username: String = authentication.name
        val currentDate: Date = Date()
        val expiryDate: Date = Date(currentDate.time + JWT_EXPIRATION)

        val token: String = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact()

        return token
    }

    fun getUsernameFromJwt(token: String): String {
        val claims: Claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJwt(token)
                .body

        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            getUsernameFromJwt(token)
            return true
        } catch (ex: Exception) {
            throw AuthenticationCredentialsNotFoundException("JWT was expired or incorrect")
        }
    }
}