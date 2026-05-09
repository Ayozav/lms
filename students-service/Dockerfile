# ---- Этап 1: сборка fat JAR через ShadowJar ----
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Копируем Gradle Wrapper и скрипты сборки
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Используем китайское зеркало для Gradle distribution
RUN mkdir -p /root/.gradle/wrapper/dists && \
    echo "distributionUrl=https://mirrors.cloud.tencent.com/gradle/gradle-8.13-bin.zip" > gradle/wrapper/gradle-wrapper.properties

# Копируем исходники
COPY src ./src

# Даём права на выполнение и собираем shadowJar (тесты пропускаем)
RUN chmod +x gradlew && ./gradlew shadowJar -x test

# ---- Этап 2: минимальный образ для запуска ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем shadowJar (обычно имя заканчивается на '-all.jar')
COPY --from=builder /app/build/libs/*-all.jar app.jar

HEALTHCHECK --interval=60s --timeout=15s --start-period=90s --retries=5 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:4040/echo || exit 1

# Порт, который слушает ваше приложение (по умолчанию 4040)
EXPOSE 4040
# Точка входа
CMD ["java", "-jar", "app.jar"]
