# How to run junit-platform-console-standalone #
1. Go to the project to be tested root directory.

java -jar <path-to-junit-console-jar>/junit-platform-console-standalone-1.6.2.jar --classpath /build/classes/java/test:/build/classes/java/main -c <package.TestClassName>

Ex:
1. Go to LoanQuotes directory
2. Assume that junit-console is its parent directory.

java -jar ../junit-platform-console-standalone-1.6.2.jar --classpath /build/classes/java/test:/build/classes/java/main -c app.AppTest
