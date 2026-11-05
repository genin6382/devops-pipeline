
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    environment {
        AWS_REGION = 'ap-southeast-2'
        ECR_REPOSITORY = 'devops/cia'
        IMAGE_TAG = '1.0'
        AWS_ACCOUNT_ID = '803109508882'
        ECR_REGISTRY = '803109508882.dkr.ecr.ap-southeast-2.amazonaws.com'
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

        stage('Docker Push to ECR') {
            steps {
                echo 'Logging into Amazon ECR'

                bat """
                    aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %ECR_REGISTRY%
                """

                echo 'Tagging Docker image'

                bat """
                    docker tag order-management:1.0 %ECR_REGISTRY%/%ECR_REPOSITORY%:%IMAGE_TAG%
                """

                echo 'Pushing Docker image to ECR'

                bat """
                    docker push %ECR_REGISTRY%/%ECR_REPOSITORY%:%IMAGE_TAG%
                """
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
