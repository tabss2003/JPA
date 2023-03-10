# JPA

쇼핑몰

## ✔ 엔티티 분석

![jpa회원엔티티분석](https://user-images.githubusercontent.com/43884708/223142892-76a6ea98-b8b0-48ca-aca8-ec22eb834b7b.png)
<br>
<br>

## ✔ 기술 스택
- IntelliJ IDEA 2022.3.1 (Ultimate Edition) : 17.0.5+1-b653.23 amd64
- Spring Web
- SpringBoot
- H2 Database

## ✔ 기능
- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록
  - 상품 수정
  - 상품 조회
 - 주문 기능
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
  
<br>

## ✔ 애플리케이션 아키텍처

![애플리케이션아키텍처](https://user-images.githubusercontent.com/43884708/223147511-6675c9ef-e0bb-4edc-ac15-fdf411ee9f2a.png)
### 계층형 구조 사용

- controller,web : 웹계층
- service :비지니스 로직, 트랜잭션 처리
- repository :JPA를 직접 사용하는 계층, 엔티티 매니저 사용
- domain : 엔티티가 모여 있는 계층, 모든 계층에서 사용

### 패키지 구조

- jpabook.jpashop
    - domain
    - exception
    - repository
    - service
    - web

<br>


## [엔티티 설계시 주의점](https://meteor-witness-ff5.notion.site/b098112055bf41f8ae9d89e277cb24f8)
- 엔티티에는 가급적 Setter를 사용하지 말자
- 모든 연관관계는 지연로딩으로 설정!
- 컬렉션은 필드에서 초기화하자
- 테이블, 컬럼명 생성 전략
