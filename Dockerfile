FROM amazoncorretto:11-alpine as builder
WORKDIR /app

COPY ./build.gradle ./settings.gradle ./gradlew ./
COPY ./gradle ./gradle
#윈도우 시스템인 경우를 위해 dos2unix 실행
RUN dos2unix gradlew
RUN ./gradlew build -x test --parallel --continue > /dev/null 2>&1 || true

COPY ./src ./src
RUN ./gradlew build -x test --parallel

FROM amazoncorretto:11-alpine
COPY --from=builder /app/build/libs/BaGulBaGul-0.0.1-SNAPSHOT.jar ./
#db컨테이너 실행을 기다리기 위함
COPY ./wait-for-it.sh ./wait-for-it.sh
#wait-for-it 실행을 위해 bash가 필요
RUN apk add --no-cache bash
RUN dos2unix ./wait-for-it.sh
RUN chmod +x wait-for-it.sh

ENTRYPOINT ["java", "-jar", "BaGulBaGul-0.0.1-SNAPSHOT.jar"]