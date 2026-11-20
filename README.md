# Demo Java App

[![GitHub Actions CI](https://img.shields.io/github/actions/workflow/status/reiloe/demo-java-app/ci.yml?style=for-the-badge&logo=github&label=GitHub%20CI)](https://github.com/reiloe/demo-java-app/actions/workflows/ci.yml)
[![GitLab Pipeline Status](https://img.shields.io/gitlab/pipeline-status/reilo2401%2Fdemo-java-app?branch=main&style=for-the-badge&logo=gitlab&label=GitLab%20CI)](https://gitlab.com/reilo2401/demo-java-app/-/pipelines)
[![Code Coverage](https://img.shields.io/gitlab/pipeline-coverage/reilo2401%2Fdemo-java-app?branch=main&style=for-the-badge&logo=gitlab&label=Coverage)](https://gitlab.com/reilo2401/demo-java-app/-/pipelines)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.9-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-6-25A162?style=for-the-badge&logo=junit5&logoColor=white)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

A clean Java 21 command-line application for my developer portfolio that calculates and displays the remaining time until a user-defined goal deadline. This repository is intentionally structured to be built and tested using **both Apache Maven and Gradle without duplicating source code**.

### Example Usage

**Interactive Mode:**
```text
Enter your goal: Finish Cloud Certification
Enter the deadline (YYYY-MM-DD): 2027-06-01
Enter start date (YYYY-MM-DD, press Enter for today): 2026-06-01

Remaining time until 'Finish Cloud Certification': 0 year(s), 6 month(s), 12 day(s) (193 total days).
Progress: [██████████░░░░░░░░░░] 47% elapsed
```

**Non-Interactive CLI Mode (for scripts & automation):**
```bash
java -jar target/demo-java-app-1.0.0.jar "Finish Cloud Certification" 2027-06-01 2026-06-01
```

---

## Key Highlights

* **Dual Build Setup:** • Includes both a pom.xml (centralized <properties>) and a build.gradle.kts (backed by gradle/libs.versions.toml) alongside their wrappers (mvnw / gradlew), requiring no pre-installed build tools.
* **Modern Java 21 Architecture:** Utilizes sealed interfaces, record types, switch pattern matching, and java.time APIs with deterministic unit tests.
* **Interactive & Scriptable CLI:** Supports both guided console prompts (with input validation loops) and direct command-line arguments.
* **Multi-Hoster & Dual CI/CD:** Mirrored simultaneously on **GitHub** and **GitLab**. Every push automatically triggers both **GitHub Actions** and **GitLab CI/CD** pipelines to verify the Maven and Gradle builds.
* **Test Coverage Ready:** Designed to integrate cleanly with JaCoCo for automated test coverage reporting across both build systems.
* **Automated Documentation:** Includes a zero-dependency Python script (`scripts/update-tree.py`) to keep the project directory tree in this README up to date (can be run manually or wired into a local Git `pre-commit` hook).

---

## Prerequisites

* **Java Development Kit (JDK) 21** or higher (Gradle will automatically provision a matching JDK via toolchains if none is found locally).

---

## Building & Testing

Clone the repository from [GitHub](https://github.com/reiloe/demo-java-app) or [GitLab](https://gitlab.com/reilo2401/demo-java-app):

```bash
# Clone from GitHub
git clone https://github.com/reiloe/demo-java-app.git

# Or clone from GitLab
git clone https://gitlab.com/reilo2401/demo-java-app.git

cd demo-java-app
```

### Option A: Using Maven

**Run tests:**
```bash
./mvnw test
```

**Build & run the executable JAR:**
```bash
./mvnw clean package
java -jar target/demo-java-app-1.0.0.jar
```

### Option B: Using Gradle

**Run tests:**
```bash
./gradlew test
```

**Build & run the executable JAR:**
```bash
./gradlew clean build
java -jar build/libs/demo-java-app-1.0.0.jar
```

---

## Project Structure

*Maintained via `scripts/update-tree.py` (run `python3 scripts/update-tree.py` or configure a local Git `pre-commit` hook to update automatically before committing):*

<!-- TREE_START -->
```text
demo-java-app/
├── .github/workflows/
│   └── ci.yml
├── .mvn/
├── gradle/
│   └── libs.versions.toml
├── scripts/
│   └── update-tree.py
├── src/
│   ├── main/java/de/reilo/countdown/
│   │   ├── CountdownResult.java
│   │   ├── CountdownService.java
│   │   └── Main.java
│   └── test/java/de/reilo/countdown/
│       └── CountdownServiceTest.java
├── .gitignore
├── .gitlab-ci.yml
├── build.gradle.kts
├── gradlew
├── gradlew.bat
├── LICENSE
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── settings.gradle.kts
```
<!-- TREE_END -->

---

## License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## Author

* **GitHub:** [@reiloe](https://github.com/reiloe)
* **GitLab:** [@reilo2401](https://gitlab.com/reilo2401)
