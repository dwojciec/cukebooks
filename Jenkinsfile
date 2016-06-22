node {
  stage 'Build and Test'
  checkout scm
  ['phantomjs', 'firefox', 'chrome'].each { browser ->
    sh "./grailsw -Dgeb.env=${browser} test-app"
  }
}

