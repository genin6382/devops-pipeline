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
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        PATH = '/usr/lib/jvm/java-21-openjdk-amd64/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Cloning source code from Git'
                checkout scm
            }
        }

        stage('Java Diagnostic') {
            steps {
                sh '''
                    echo "===== JAVA ENVIRONMENT ====="
                    echo "JAVA_HOME=$JAVA_HOME"
                    echo "PATH=$PATH"

                    echo "===== JAVA ====="
                    which java
                    readlink -f $(which java)
                    java -version

                    echo "===== JAVAC ====="
                    which javac
                    readlink -f $(which javac)
                    javac -version

                    echo "===== JAVA 21 DIRECT CHECK ====="
                    $JAVA_HOME/bin/java -version
                    $JAVA_HOME/bin/javac -version
                    $JAVA_HOME/bin/javac --release 21 -version

                    echo "===== MAVEN JAVA VERSION ====="
                    ./mvnw -version
                '''
            }
       }

        stage('Build') {
            steps {
                echo 'Compiling the Spring Boot application'
                sh './mvnw -B -DskipTests clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests'
                sh './mvnw -B test'
            }
            post {
                always {
                   junit allowEmptyResults: true,
                        testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application into a JAR'
                sh './mvnw -B -DskipTests package'
                archiveArtifacts artifacts: 'target/*.jar',
                                     fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building the Docker image'
                sh 'docker build -t order-management:1.0 .'
            }
        }

        stage('Docker Push to ECR') {
            steps {
                echo 'Logging into Amazon ECR'

                sh """
                    aws ecr get-login-password --region \$AWS_REGION | docker login --username AWS --password-stdin \$ECR_REGISTRY
                """

                echo 'Tagging Docker image'

                sh """
                    docker tag order-management:1.0 \$ECR_REGISTRY/\$ECR_REPOSITORY:\$IMAGE_TAG
                """

                echo 'Pushing Docker image to ECR'

                sh """
                    docker push \$ECR_REGISTRY/\$ECR_REPOSITORY:\$IMAGE_TAG
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