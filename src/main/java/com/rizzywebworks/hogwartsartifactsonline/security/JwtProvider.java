package com.rizzywebworks.hogwartsartifactsonline.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // nimbus will be injected from config into above and used down below
    public String createToken(Authentication authentication) {
        Instant now = Instant.now(); // issued at time
        long expiresIn = 2; // 2 hours

        // prepare a claim called authorities, so we are building back the space separated string
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining( " ")); // MUST Be space-delimited.

        //build claim
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self") // everything done by one server , we are not using a dedicated authorization server
                .issuedAt(now)
                .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("authorities", authorities) // custom claims
                .build();

        // encode the claims
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
