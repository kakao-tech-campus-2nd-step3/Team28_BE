# DevCard

DevCard는 개발자를 위한 디지털 명함 서비스입니다. 사용자들은 자신의 프로필, GitHub 레포지토리, 커밋 활동 등을 포함한 디지털 명함을 생성하고 공유할 수 있습니다.

## 목차

- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [설치 및 실행](#설치-및-실행)
- [사용 방법](#사용-방법)
- [테스트](#테스트)
- [구성원](#구성원)
- [라이선스](#라이선스)

## 프로젝트 소개

DevCard는 개발자들이 자신의 정보를 효율적으로 공유할 수 있도록 돕는 서비스입니다. GitHub OAuth2 인증을 통해 간편하게 로그인하고, 자신의 GitHub 활동 내역을 명함에 포함시킬 수 있습니다.

## 기술 스택

- **백엔드**: Java 17, Spring Boot 3.1.2
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL 8
- **ORM**: Spring Data JPA (Hibernate)
- **테스트**: JUnit 5, Mockito
- **CI/CD**: GitHub Actions, AWS CodeDeploy
- **인증**: Spring Security, OAuth2 (GitHub)
- **프론트엔드**: HTML, CSS, JavaScript, jQuery

## 설치 및 실행

### 전제 조건

- Java 17 이상
- Gradle 7.x 이상
- MySQL 8.x
- Git

### 클론 및 환경 설정

```bash
# 레포지토리 클론
git clone https://github.com/your-username/DevCard.git
cd DevCard

# 필요한 환경 변수 설정 (.env 파일 또는 시스템 환경 변수)
# 예시로 .env 파일을 사용하는 경우
cp .env.example .env
```

### 구성원

- 팀장 : 이동현 (BE - 명함 기능 구현)
- 팀원 : 박상우 (BE - 채팅 기능 구현)
- 팀원 : 김해경 (BE - 명함 기능 구현)
- 팀원 : 이경빈 (BE - 채팅 기능 구현)
- 팀원 : 박서현 (BE - 인증 기능 구현)
