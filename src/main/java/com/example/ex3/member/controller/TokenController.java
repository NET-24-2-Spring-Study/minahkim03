package com.example.ex3.member.controller;

import com.example.ex3.member.dto.MemberDTO;
import com.example.ex3.member.security.JWTUtil;
import com.example.ex3.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@Log4j2
@RequiredArgsConstructor
public class TokenController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody MemberDTO memberDTO) {
        log.info("make token...............");

        MemberDTO memberDTOResult = memberService.read(memberDTO.getMid(), memberDTO.getMpw());

        log.info(memberDTOResult);

        String mid = memberDTOResult.getMid();

        Map<String, Object> dataMap = memberDTOResult.getDataMap();

        String accessToken = jwtUtil.createToken(dataMap, 10);
        String refreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
        @RequestHeader("Authorization") String accessTokenStr,
        @RequestHeader("refreshToken") String refreshToken,
        @RequestParam("mid") String mid
    ) {
        log.info("access token with Bearer..............." + accessTokenStr);

        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")) {
            return handleException("No Access Token", 400);
        }

        if (refreshToken == null) {
            return handleException("No Refresh Token", 400);
        }

        log.info("refresh token............." + refreshToken);

        if (mid == null) {
            return handleException("No Mid", 400);
        }

        //Access Token 만료되었는지 확인
        String accessToken = accessTokenStr.substring(7);
        try {
            jwtUtil.validateToken(accessToken);
            Map<String, String> data = makeData(mid, accessToken, refreshToken);
            return ResponseEntity.ok(data);
        } catch (ExpiredJwtException expiredJwtException) {
            //Refresh 필요
            Map<String, String> newTokenMap = makeNewToken(mid, refreshToken);
            return ResponseEntity.ok(newTokenMap);
        } catch(Exception e) {
            return handleException(e.getMessage(), 400);
        }
    }

    private Map<String, String> makeData(String mid, String accessToken, String refreshToken) {
        return Map.of("mid", mid, "accessToken", accessToken, "refreshToken", refreshToken);
    }

    private Map<String, String> makeNewToken(String mid, String refreshToken) {
        try {
            Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
            if (!mid.equals(claims.get("mid").toString())) {
                throw new RuntimeException("Invalid Refresh Token Host");
            }
            MemberDTO memberDTO = memberService.getByMid(mid);
            Map<String, Object> newClaims = memberDTO.getDataMap();
            String newAccessToken = jwtUtil.createToken(newClaims, 10);
            String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

            return makeData(mid, newAccessToken, newRefreshToken);
        } catch (Exception e) {
            handleException(e.getMessage(), 400);
        }
        return null;
    }

    private ResponseEntity<Map<String, String>> handleException(String msg, int status) {
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }
}
