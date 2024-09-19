package com.example.ex3.member.repository;

import com.example.ex3.member.entity.MemberEntity;
import com.example.ex3.member.exception.MemberExceptions;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testInsert() {
        for (int i = 1; i <= 100; i++) {
            MemberEntity memberEntity = MemberEntity.builder()
                .mid("user"+i)
                .mpw(passwordEncoder.encode("1111"))
                .mname("USER"+i)
                .email("user"+i+"@aaa.com")
                .role(i <= 80 ? "USER": "ADMIN")
                .build();
            memberRepository.save(memberEntity);
        }
    }

    @Test
    public void testRead() {

        String mid = "user1";

        Optional<MemberEntity> result = memberRepository.findById(mid);

        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        System.out.println(memberEntity);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        String mid = "user1";

        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);
        memberEntity.changePassword(passwordEncoder.encode("2222"));
        memberEntity.changeName("USER1-1");
    }

    @Test
    @Transactional
    @Commit
    public void testDelete() {
        String mid = "user1";
        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(() -> MemberExceptions.NOT_FOUND.get());
        memberRepository.delete(memberEntity);
    }

    
}
