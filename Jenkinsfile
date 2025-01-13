pipeline {
  agent any
  stages{
    stage('Build'){
      steps{
        echo 'Building...'
        sh "pwd"
      }
    }
    stage('Test'){
      steps{
        echo 'Testing...'
       sh "ls"
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
