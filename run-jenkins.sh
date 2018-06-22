docker run \
  --name jenkins \
  -u root \
  --rm \
  -d \
  -p 80:8080 \
  -v jenkins-data:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkinsci/blueocean
