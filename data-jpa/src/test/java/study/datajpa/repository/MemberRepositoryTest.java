package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember =  memberRepository.findById(savedMember.getId()).get();

        assertEquals(findMember.getId(),member.getId());
        assertEquals(findMember.getUsername(),member.getUsername());
        assertEquals(findMember,member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertEquals(findMember1,member1);
        Assertions.assertEquals(findMember2,member2);

        //리스트 조회
        List<Member> all = memberRepository.findAll();
        Assertions.assertEquals(all.size(),2);

        //카운트
        long count = memberRepository.count();
        Assertions.assertEquals(count,2);

        //삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        Assertions.assertEquals(deleteCount,0);

    }

}