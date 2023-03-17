package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    /**
     * 장점 : 테스트 코드 작성할 때 몫(가짜 repository)을 주입할 수 있음
     * 단점 : runtime 시점에 누군가 값을 바꿀 수 있음
     @Autowired
     public void setMemberRepository(MemberRepository memberRepository){
     this.memberRepository = memberRepository;
     }
      * */

    /**
     * 회원 가입
     * @Transactional : 쓰기
     * */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.seve(member);
        return member.getId();
    }

    /**
     * 동시에 같은 닉네임으로 가입하는 상황을 대비해서 DB에서 닉네임 unique 값으로 설정하는 것을 권장
     * */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * @Transactional(readOnly = true) : 읽기
     * */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
