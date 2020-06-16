# How to run junit-platform-console-standalone #
java -jar junit-platform-console-standalone-1.6.2.jar --classpath **<ProjectRoot>**/build/classes/java/test:**<ProjectRoot>**/build/classes/java/main -c **<package.TestClassName>**

Ex:
java -jar junit-platform-console-standalone-1.6.2.jar --classpath **LoanQuotes**/build/classes/java/test:**LoanQuotes**/build/classes/java/main -c **app.AppTest**
