pipeline {
    agent any
    stages {
        stage('Test Ecommerce discovery server') {
            steps {
                git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform-eureka-discovery-server.git'
                bat 'mvn test'
            }
        }
        stage('Build Ecommerce discovery server') {
                    steps {
                        git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform-eureka-discovery-server.git'
                        bat 'mvn clean install -DskipTests'
                    }
        }
        stage('Test Back-Office service') {
                    steps {
                        git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-Platform-BackOffice.git'
                        bat 'mvn test'
                    }
        }
        stage('Build Back-Office service') {
                    steps {
                        git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-Platform-BackOffice.git'
                        bat 'mvn clean install -DskipTests'
                    }
        }
        stage('Test Ecommerce-platform service') {
                    steps {
                        git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'mvn test'
                    }
        }
        stage('Build Ecommerce-platform service') {
                    steps {
                        git branch: 'master', credentialsId: 'fa48c95b-ef28-4fe9-bb77-c6008be5aa3d', url: 'https://github.com/asen1995/eCommerce-platform.git'
                        bat 'mvn clean install -DskipTests'
                    }
        }
    }
}
