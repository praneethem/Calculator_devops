pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE = 'praneethem/calculator-api'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: false
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        set +x
                        printf '%s' "$DOCKERHUB_TOKEN" | docker login \
                            --username "$DOCKERHUB_USER" \
                            --password-stdin
                        docker push "${DOCKER_IMAGE}:${BUILD_NUMBER}"
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    export IMAGE_NAME="$DOCKER_IMAGE"
                    export IMAGE_TAG="$BUILD_NUMBER"

                    docker compose pull calculator
                    docker compose up -d --no-build --remove-orphans
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    set -eu

                    for attempt in $(seq 1 30); do
                        if curl -fsS \
                            http://localhost:8088/actuator/health; then
                            echo
                            echo "Deployment health check passed."
                            exit 0
                        fi

                        echo "Waiting for API health... attempt $attempt/30"
                        sleep 2
                    done

                    echo "Deployment health check failed."
                    docker compose ps
                    docker compose logs --tail=100 calculator nginx
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Build, tests, Docker publishing, deployment, and health check succeeded!'
        }
        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
        always {
            sh 'docker logout >/dev/null 2>&1 || true'
        }
    }
}
