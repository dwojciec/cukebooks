#!groovy

// Requires script approval:
//   method java.util.Collection addAll java.util.Collection
//   staticMethod java.net.InetAddress getLocalHost
//   method java.net.InetAddress getHostName

node {
  stage 'Build and Unit Test'
  checkout scm
  withJavaEnv {
    sh "./grailsw test-app unit: integration:"
    //step $class: 'JUnitResultArchiver', testResults: '**/test-reports/**/TEST*.xml'
    sh "./grailsw war"
  }

  stage 'Functional Test'
  stash name: 'test_sources'
  parallel ['chrome', 'firefox'].collectEntries { browser -> [ browser: {
    node {
      unstash name: 'test_sources'
      withJavaEnv { sh "./grailsw -noreloading -Ddisable.auto.recompile=true -Dgrails.server.host=${InetAddress.localHost.hostName} -Dgeb.env=remote -Dgeb.url=http://hub:4444/wd/hub -Dgeb.browser=browserName=${browser} test-app" }
    }
  } ] }

  //only deploy when on master branch
  if (env.BRANCH_NAME == 'master') {
    stage 'Deploy'
    withJavaEnv { sh './grailsw deploy' }
  }

}

/* This code shame-lessly copied and pasted from some Jenkinsfile code abayer
   wrote for the jenkinsci/jenkins project */
void withJavaEnv(List envVars = [], def body) {
    String jdktool = tool name: "jdk8", type: 'hudson.model.JDK'

    // Set JAVA_HOME, and special PATH variables for the tools we're
    // using.
    List javaEnv = ["PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}"]

    // Add any additional environment variables.
    javaEnv.addAll(envVars)

    // Invoke the body closure we're passed within the environment we've created.
    withEnv(javaEnv) {
        body.call()
    }
}

