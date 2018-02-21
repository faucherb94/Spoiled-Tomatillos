pipeline {
	agent {
		docker {
			image 'maven:3-alpine'
			args '-v /root/.m2:/root/.m2 -e DB_PASS=$DB_PASSWORD'
		}
	}
	stages {
		stage('Build') {
			steps {
				echo "Building"
				sh 'mvn compile'
				sh 'mvn package'
			}
		}
		stage('Test') {
			steps {
				echo "Testing"
				sh 'mvn test'
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
					sh 'scp -o StrictHostKeyChecking=no -i $PEM_PATH $WORKSPACE/target/cs4500-spring2018-team26-1.war ec2-user@app.codersunltd.me:~/app/cs4500-spring2018-team26-1.war'
					sh 'ssh -o StrictHostKeyChecking=no -i $PEM_PATH ec2-user@app.codersunltd.me \'nohup java -jar app/cs4500-spring2018-team26-1.war > app.out 2>&1 &\''
				}
			}
		}
	}
}