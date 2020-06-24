FROM openjdk:15-jdk-alpine3.11
COPY ./build/libs/LoanQuotes.jar LoanQuotes.jar
COPY ./lenders.csv lenders.csv
ENTRYPOINT ["java", "-jar", "LoanQuotes.jar"]