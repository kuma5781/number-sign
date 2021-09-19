pipeline {
  agent {
    label 'worker'
  }
  options {
    timeout(time: 30, unit: 'MINUTES')
  }
  stages {
    stage('Init conf file') {
      steps {
        sh 'docker-compose down --remove-orphans'
        sh "\\cp -f src/test/resources/test_db.conf.sample src/test/resources/test_db.conf"
      }
    }
    stage('Run yamory for PR') {
      when {
        not {
          branch 'master'
        }
      }
      steps {
        withCredentials([string(credentialsId: 'b946c17a-cd8d-44b5-b2d1-f8045d9ebecc', variable: 'YAMORY_API_KEY')]) {
          sh 'docker run --env-file .env.ci --volume $PWD:/opt/universe-bidder-rtb --workdir /opt/universe-bidder-rtb --rm hseeberger/scala-sbt:8u242_1.3.8_2.12.10 sbt -J-Dsbt.log.noformat=true dependencyTree | PROJECT_GROUP_KEY=universe-bidder-proto_PR YAMORY_API_KEY=$YAMORY_API_KEY bash -c "$(curl -sSf -L https://localscanner.yamory.io/script/sbt)"'
        }
      }
    }
    stage('Run yamory') {
      when {
        branch 'master'
      }
      steps {
        withCredentials([string(credentialsId: 'b946c17a-cd8d-44b5-b2d1-f8045d9ebecc', variable: 'YAMORY_API_KEY')]) {
          sh 'docker run --env-file .env.ci --volume $PWD:/opt/universe-bidder-rtb --workdir /opt/universe-bidder-rtb --rm hseeberger/scala-sbt:8u242_1.3.8_2.12.10 sbt -J-Dsbt.log.noformat=true "project universeBidderProto" dependencyTree | PROJECT_GROUP_KEY=universe-bidder-proto YAMORY_API_KEY=$YAMORY_API_KEY bash -c "$(curl -sSf -L https://localscanner.yamory.io/script/sbt)"'
        }
      }
    }
    stage('Check code style') {
      steps {
        sh 'docker-compose run --rm test-universe-bidder sbt "all universeBidderProto/scalafmtCheckAll profiler/scalafmtCheckAll"'
      }
    }
    stage('Run test') {
      steps {
        script {
          try {
            sh 'docker-compose build --no-cache'
            sh 'docker-compose run --rm test-universe-bidder sbt clean test'
          } finally {
            sh 'docker-compose down --remove-orphans'
          }
        }
      }
    }
  }
}
