# Variables :

### username : 아이디
### password : 비밀번호
### nickname : 닉네임
### title : 글 제목
### content : 내용
### writer : 글쓴이 (nickname 말고 username으로 !)
그래야 서버에서 아이디를 기반으로 사용자를 찾을 수 있음
(추후에 토큰에서 꺼내는 방식으로 교체 예정)

# Mapping :
### ../auth/signup : 회원가입
### ../auth/login : 로그인
### ../api/boards : 게시판 열람

# 요청 방식 :
## POST :
### 글쓰기 : Header에 토큰을 실어서 보내야 함
### 회원가입 : username, password, nickname 서버에 전송하여 데이터베이스에 저장
### 로그인 : username, password 서버에 전송하여 데이터베이스에 있는 데이터와 비교

## GET :
### 게시판 데이터 불러오기 : ../api/boards
게시판 데이터 불러올 때도 토큰 확인해야 함 (Header에 토큰 실어서 보내기)
### 