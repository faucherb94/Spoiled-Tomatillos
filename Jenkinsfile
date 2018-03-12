pipeline {
  environment {
  	ARTIF_ID = readMavenPom().getArtifactId()
  	VERSION = "0.0." + env.BUILD_NUMBER
  	ARTIFACT = ARTIF_ID + "-" + VERSION
  }
  
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2 -e DB_PASS=$DB_PASSWORD'
    }
  }

  stages {
    stage('Build') {
      steps {
        notifyBuild('STARTED')
        script {
          def pom = readMavenPom
          pom.setVersion($VERSION)
          writeMavenPom model: pom
        }
        echo "Building"
        sh 'mvn compile'
        sh 'mvn package -Dmaven.test.skip=true'
        echo "Built artifact $ARTIFACT"
      }
    }

    stage('Test') {
      steps {
        echo "Testing"
        sh 'mvn test'
      }
    }
    
    stage('SonarQube') {
      steps {
        withSonarQubeEnv('SonarQube') {
          sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true'
          sh 'mvn sonar:sonar'
        }
      }
    }
    
    stage('Quality') {
      steps {
        sh 'sleep 30'
        timeout(time: 10, unit: 'SECONDS') {
          retry(5) {
            script {
              def qg = waitForQualityGate()
              if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
              }
            }
          }
        }
      }
    }

    stage('Deploy') {
      when { branch 'master'}
      steps {
        checkout scm
        echo 'Deploying...'
        withCredentials ([file(credentialsId: 'CS4500_AWS_PEM_File', variable: 'PEM_PATH')]) {
          sh 'apk add -U --no-cache openssh'
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'pkill -f team26 > /dev/null 2>&1 &\''
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'mkdir -p app > /dev/null 2>&1 &\''
          sh 'scp -o StrictHostKeyChecking=no -i $PEM_PATH $WORKSPACE/target/$ARTIFACT ec2-user@app.codersunltd.me:~/app/$ARTIFACT'
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'nohup java -jar app/$ARTIFACT > app.out 2>&1 &\''
        }
      }
    }
  }
  
  post {
    always {
      notifyBuild(currentBuild.result)
      archiveArtifacts artifacts: 'target/*.war', fingerprint: true
      junit 'target/surefire-reports/*.xml'
    }
  }
}

def notifyBuild(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  // Default values
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)
}