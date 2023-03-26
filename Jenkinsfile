pipeline {
    agent any

    triggers {
            cron('30 23 * * *') //run at 23:30:00
        }

    stages {

        stage('Stop deployment server') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'docker-compose down'
                    }
        }

        stage('Test Ecommerce discovery server') {
            steps {
                git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform-eureka-discovery-server.git'
                bat 'mvn test'
            }
        }
        stage('Build docker Ecommerce discovery server image') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform-eureka-discovery-server.git'
                        bat 'mvn clean install -DskipTests'
                        bat 'docker rmi ecommerce-discovery-server-image'
                        bat 'docker build --build-arg VERSION=0.0.1-SNAPSHOT -t ecommerce-discovery-server-image .'
                    }
        }
        stage('Test Back-Office service') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-Platform-BackOffice.git'
                        bat 'mvn test'
                    }
        }
        stage('Build docker Back-Office service image') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-Platform-BackOffice.git'
                        bat 'mvn clean install -DskipTests'
                        bat 'docker rmi ecommerce-back-office-service-image'
                        bat 'docker build --build-arg VERSION=0.0.1-SNAPSHOT -t ecommerce-back-office-service-image .'
                    }
        }
        stage('Test Ecommerce-platform service') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'mvn test'
                    }
        }
        stage('Build docker Ecommerce-platform service image') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'mvn clean install -DskipTests'
                        bat 'docker rmi ecommerce-platform-image'
                        bat 'docker build --build-arg VERSION=0.0.1-SNAPSHOT -t ecommerce-platform-image .'
                    }
        }
        stage('Deploy') {
                    steps {
                        git branch: 'docker', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'docker-compose up -d'
                    }
        }
    }
}
