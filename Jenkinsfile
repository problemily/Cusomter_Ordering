pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS = 'github-token'  
        REPO_URL = 'https://github.com/problemily/Cusomter_Ordering' 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    // Notify GitHub that the build is pending
                    githubNotify context: 'Build', status: 'PENDING', description: 'Build started', targetUrl: "${env.BUILD_URL}"
                }

                // Run your build steps here
                echo 'Running build...'
              sh "pwd"
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        // Notify GitHub that the build is in progress
                        githubNotify context: 'Tests', status: 'PENDING', description: 'Tests started', targetUrl: "${env.BUILD_URL}"
                        
                        // Simulate running tests
                        echo 'Running tests...'
                        sh 'ls' // Simulate a passing test
                        
                        // Notify GitHub of success
                        githubNotify context: 'Tests', status: 'SUCCESS', description: 'All tests passed', targetUrl: "${env.BUILD_URL}"
                    } catch (e) {
                        // Notify GitHub of failure
                        githubNotify context: 'Tests', status: 'FAILURE', description: 'Tests failed', targetUrl: "${env.BUILD_URL}"
                        throw e
                    }
                }
               stage('Deploy'){
                steps{
                  echo 'Deploying...'
                  sh "touch newFile.txt"
                  }
            }
        }
    }

    post {
        success {
            script {
                // Notify GitHub of success after pipeline completes
                githubNotify context: 'Pipeline', status: 'SUCCESS', description: 'Pipeline completed successfully', targetUrl: "${env.BUILD_URL}"
            }
        }
        failure {
            script {
                // Notify GitHub of failure after pipeline completes
                githubNotify context: 'Pipeline', status: 'FAILURE', description: 'Pipeline failed', targetUrl: "${env.BUILD_URL}"
            }
        }
    }
}
}





























// pipeline {
//   agent any
//   stages{
//     stage('Build'){
//       steps{
//         echo 'Building...'
//         sh "pwd"
//       }
//     }
//     stage('Test'){
//       steps{
//         echo 'Testing...'
//        sh "ls"
//       }
//     }
//     stage('Deploy'){
//       steps{
//         echo 'Deploying...'
//         sh "touch newFile.txt"
//       }
//     }
//   }
// }
