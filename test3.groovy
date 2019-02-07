#!/usr/bin/env groovy
//Calling another function

node('poc1'){
  stage("Calling function"){
   
    def rootDir= pwd()
    println("Current Directory: " + rootDir)
    // point to exact source file
    def example = load "${rootDir}/pipeline/test4.groovy"
    echo "calling test function present in different file but in same repo"
    echo "calling git checkout function present in same file"
    example.git_checkout()
    sleep 2
    example.testFunction()
    echo "Done calling test function"
  }
}
    
