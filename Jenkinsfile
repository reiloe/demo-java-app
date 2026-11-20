pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    environment {
        IMAGE_NAME = 'demo-java-app'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        IMAGE_TAG = "${env.BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'local'}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test with Maven') {
            steps {
                sh 'mvn -B clean package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Build & Push Docker Image') {
            when {
                anyOf {
                    branch 'main'
                    expression { env.BRANCH_NAME == null }
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        docker build -t "\$DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}" -t "\$DOCKER_USER/${IMAGE_NAME}:latest" .
                        echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                        docker push "\$DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}"
                        docker push "\$DOCKER_USER/${IMAGE_NAME}:latest"
                    """
                }
            }
        }
    }

    post {
        always {
            withCredentials([usernamePassword(
                credentialsId: "${DOCKERHUB_CREDENTIALS_ID}",
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )]) {
                sh """
                    docker logout || true
                    docker rmi "\$DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}" "\$DOCKER_USER/${IMAGE_NAME}:latest" || true
                """
            }
        }
    }
}
