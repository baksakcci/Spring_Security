# Spring_Security
Spring Security 실습

---
# Login API URL
- [Google](https://console.cloud.google.com/projectcreate)
- [Facebook](https://developers.facebook.com/docs/facebook-login/)
- [Naver]()
- [Kakao]()


---
# Application.yml
- Google
```yaml
security:
  oauth2:
    client:
      registration:
        google: 
          client-id: ???
          client-secret: ???
          scope:
            - email
            - profile
            - 추가
```
---
# Dependency
```
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-mustache'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation group: 'org.springframework.security', name: 'spring-security-oauth2-client'
```
