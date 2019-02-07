#!/usr/bin/env groovy
//Calling another function
def github_org = 'sainavya5'
def github_repo = 'pipeline'
def github_repo_branch = 'master'

node('poc1'){
  stage("Calling function"){
    echo "calling git checkout function present in same file"
    git_checkout()
    sleep 2
    echo "After calling git checkout function"
    echo "listing contents in git repo"
    sh """
      echo "listing contents in current folder after checkout"
      ls
      echo "cd into pipeline repo"
      cd pipeline
      echo "listing contents in pipeline repo"
      ls
      """
    sh 'pwd'
    def rootDir= pwd()
    println("Current Directory: " + rootDir)
    // point to exact source file
    def example = load "${rootDir}/pipeline/test2.groovy"
    echo "calling test function present in different file but in same repo"
    example.testFunction()
    echo "after calling test function"
    
    
    
    //echo "calling test function present in different file but in same repo"
    //sh 'ls'
    //load 'pipeline/test2.groovy'
    //sh 'ls'
    //testFunction()
    //echo "after calling test function"
  }
}
     
def git_checkout()
{
    //cloning git repository
    checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${github_repo_branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [
                    [$class: 'CleanBeforeCheckout'],
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "${github_repo}"]
            ],
            submoduleCfg: [],
            userRemoteConfigs: [[
                                        credentialsId: 'github_token',
                                        url: "https://github.com/${github_org}/${github_repo}.git"
                                ]]
    ])
    echo "Done. Cloning git repository"
}
