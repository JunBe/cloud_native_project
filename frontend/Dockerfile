# 1. Nginx 이미지 기반
FROM nginx:alpine

# 2. 정적 파일 복사
COPY . /usr/share/nginx/html

# 3. 기본 포트 설정
EXPOSE 80

# 4. Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
