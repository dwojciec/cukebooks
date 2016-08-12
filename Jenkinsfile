#!groovy

node {
  stage 'Build and Unit Test'
  checkout scm
  sh "./grailsw test-app unit: integration:"
  step $class: 'JUnitResultArchiver', testResults: '**/TEST-*.xml'
  sh "./grailsw war"

  stage 'Functional Test'
  parallel 'phantomjs': {
    node { sh "./grailsw -Dgeb.env=phantomjs test-app" }
  },
  'chrome': {
    node { sh "./grailsw -Dgeb.env=chrome test-app" }
  },
  'firefox': {
    node { sh "./grailsw -Dgeb.env=firefox test-app" }
  }

  //only deploy when on master branch
  if (env.BRANCH_NAME == 'master') {
    stage 'Deploy'
    sh './grailsw deploy'
  }

}

