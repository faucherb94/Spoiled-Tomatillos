pipeline {
  environment {
  	DB_PASS       = credentials('spoiled_tomatillos_db_pass')
  	OMDB_API_KEY  = credentials('omdb_api_key')
  	TMDB_API_KEY  = credentials('tmdb_api_key')
  	VERSION       = "${env.BUILD_NUMBER}"
  }
  
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2 -e DB_PASS=$DB_PASS'
    }
  }

  stages {
  	
  	stage('Modify Pom') {
  	  steps {
  	    script {
          pom = readMavenPom()
          pom.setVersion("${VERSION}")
          def name = pom.getName() + "-${env.BRANCH_NAME}"
          def artifact = pom.getArtifactId() + "-${env.BRANCH_NAME}"
          pom.setName(name)
          pom.setArtifactId(artifact)
          ARTIFACT = pom.getArtifactId() + "-${VERSION}." + pom.getPackaging()
        }
        writeMavenPom model: pom
        echo "Defined artifact: $ARTIFACT"
  	  }
  	}
  	
    stage('Build') {
      steps {
        notifyBuild('STARTED')
        script {
          properties([
            buildDiscarder(
              logRotator(
                artifactDaysToKeepStr: '',
                artifactNumToKeepStr: '4',
                daysToKeepStr: '',
                numToKeepStr: '10'
                )
              ),
            disableConcurrentBuilds(),
            pipelineTriggers([[$class: 'PeriodicFolderTrigger', interval: '12h']])
          ])
        }
        echo "Building"
        sh 'mvn compile'
        sh 'mvn package -Dmaven.test.skip=true'
      }
    }

    stage('Test') {
      steps {
        echo "Testing"
        sh 'mvn test'
      }
    }
    
    stage('SonarQube') {
      when { not { branch 'dev' } }
      steps {
        withSonarQubeEnv('SonarQube') {
          sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=false'
          sh 'mvn sonar:sonar'
        }
      }
    }
    
    stage('Quality') {
      when { not { branch 'dev' } }
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

    stage('Dev Deploy') {
      when { branch 'dev' }
      steps {
        checkout scm
        echo 'Deploying...'
        withCredentials ([file(credentialsId: 'st_dev_deploy_pem', variable: 'PEM_PATH')]) {
          sh 'apk add -U --no-cache openssh'
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@dev.codersunltd.me \'./stopapp.sh > /dev/null 2>&1 &\''
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@dev.codersunltd.me \'./rotate.sh > /dev/null 2>&1 &\''
          sh 'scp -o StrictHostKeyChecking=no -i $PEM_PATH $WORKSPACE/target/*.war ec2-user@dev.codersunltd.me:~/app/'
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@dev.codersunltd.me \'nohup ./startapp.sh\''
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
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'./stopapp.sh > /dev/null 2>&1 &\''
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'./rotate.sh > /dev/null 2>&1 &\''
          sh 'scp -o StrictHostKeyChecking=no -i $PEM_PATH $WORKSPACE/target/*.war ec2-user@app.codersunltd.me:~/app/'
          sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'nohup ./startapp.sh\''
        }
      }
    }
  }
  
  post {
    always {
      notifyBuild(currentBuild.result)
      archiveArtifacts artifacts: 'target/*.war', fingerprint: true
      junit 'target/surefire-reports/*.xml'
      cleanWs()
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