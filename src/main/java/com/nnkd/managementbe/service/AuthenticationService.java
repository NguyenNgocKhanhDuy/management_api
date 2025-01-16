package com.nnkd.managementbe.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.AuthenticationRequest;
import com.nnkd.managementbe.dto.response.AuthenticationResponse;
import com.nnkd.managementbe.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${spring.signerKey}")
    protected String SIGNER_KEY;


    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        var user = userRepository.findUserByEmail(request.getEmail());

        if (user.isEmpty())
            throw new NoSuchElementException("No user found: "+request.getEmail());

        boolean authenticated = user.get().getEmail().equals(request.getEmail()) && user.get().getPassword().equals(hashPassword(request.getPassword()));

        if (!authenticated)
            throw new RuntimeException("Incorrect email or password");


        var token = generateToken(request.getEmail());
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(AuthenticationResponse.builder().token(token).build());

        return apiResponse;
    }

    public boolean verifyToken(String token)  {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified = signedJWT.verify(verifier);
            return verified && expirationTime.after(new Date());
        } catch (JOSEException e) {
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    public JWTClaimsSet extractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        }catch (ParseException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public String getEmailFromextractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return jwtClaimsSet.getSubject();
        }catch (ParseException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    private String generateToken(String email) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("nnkd.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(3, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String password){
        try {
            MessageDigest sha256 = null;
            sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(password.getBytes());
            BigInteger number = new BigInteger(1, hash);
            return number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
