package com.example.ex3.member.service;

import com.example.ex3.member.dto.MemberDTO;
import com.example.ex3.member.entity.MemberEntity;
import com.example.ex3.member.exception.MemberExceptions;
import com.example.ex3.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberDTO read(String mid, String mpw) {
        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);
        if (!passwordEncoder.matches(mpw, memberEntity.getMpw())) {
            throw MemberExceptions.BAD_CREDENTIALS.get();
        }
        return new MemberDTO(memberEntity);
    }

    public MemberDTO getByMid(String mid) {
        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);
        return new MemberDTO(memberEntity);
    }
}
