
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Cloning source code from Git'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling the Spring Boot application'
                bat 'mvnw.cmd -B -DskipTests clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests'
                bat 'mvnw.cmd -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application into a JAR'
                bat 'mvnw.cmd -B -DskipTests package'
                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building the Docker image'
                bat 'docker build -t order-management:1.0 .'
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing the Docker image to the registry'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    bat 'docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%'
                    bat 'docker tag order-management:1.0 your-dockerhub-username/order-management:1.0'
                    bat 'docker push your-dockerhub-username/order-management:1.0'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the failed stage logs.'
        }
        always {
            echo 'Pipeline execution finished.'
        }
    }
}
