
node{
    //defining the parameters
    parameters{
            stringParam(defaultValue: '', description: '', name: 'github_org')
            stringParam(defaultValue: '', description: '', name: 'github_repo')
            stringParam(defaultValue: '', description: '', name: 'github_repo_path')
            stringParam(defaultValue: '', description: '', name: 'github_repo_branch')
            stringParam(defaultValue: '', description: '', name: 'environment')
            //choice(choices: ['master', 'develop'], description: '', name: 'github_repo_branch')
             }
    // This stage checks to make sure the pipeline has been supplied the correct parameters.
    stage('Validation') {
            echo "=============================================="
            echo "Stage1:Validation"
            echo "=============================================="
            echo "Github Org: ${params.github_org}"
            echo "Github Repo: ${params.github_repo}"
            echo "Github Repo Path: ${params.github_repo_path}"
            echo "Github branch: ${params.github_repo_branch}"
            echo "Environment: ${params.environment}"

            echo "Entering my list"
            //creating list for parameters
            MYLIST = []
                    MYLIST += "${params.github_org}"
                    MYLIST += "${params.github_repo}"
                    MYLIST += "${params.github_repo_path}"
                    MYLIST += "${params.github_repo_branch}"
                    MYLIST += "${params.environment}"
                    //Dislay the parameter value of the parameter named "myparam"
                            //println "${params.myparam}"

                            //Dislay the name/value of all parameters
                            for(entry in params) {
                              if(entry.value == null || entry.va;ue.length() == 0)
                              {
                                echo "printing missing key"
                                println entry.key

                                //println entry.key
                              }
                              else
                              {
                                println entry.value

                                //println entry.value
                              }
                            }

                            echo "checking"

                            /*  if(MYLIST[element] == null || MYLIST[element].length() ==0)
                              {
                                  echo "Missing parameter is:"
                                  println entry.key
                              }
                              else
                              {
                                echo "The parameter validated is:"
                                println entry.value
                              }
                            }
                  /*  for (def element = 0; element < MYLIST.size(); element++) {
                            //check if each parameter is provided

                            //sh 'cut --complement -d ":" -f 1 ${MYLIST[element]}'
                            // Split MYLIST[element] on the colon if the second value trimmed is empty!
                           if(MYLIST[element] == null || MYLIST[element].length() ==0)
                            {
                                echo "The parameter missing is: ${MYLIST[element]}"
                                currentBuild.result = 'FAILURE'
                                echo "RESULT: ${currentBuild.result}"
                                sh "exit 1"
                                //error 'Fail the Build'
                            }
                            else
                              {
                                echo "The parameter validated is: ${MYLIST[element]} "
                                //currentBuild.result = 'SUCCESS'
                            }

                    }
                          // def browsers = ['chrome', 'firefox']
                    //for (int i = 0; i < browsers.size(); ++i) {
                      //  echo "Testing the ${browsers[i]} browser"*/


    }

    stage('checkout'){
        echo "=============================================="
        echo "Stage2:Checkout"
        echo "=============================================="
        sh 'echo $PWD'
        checkout([$class: 'GitSCM', branches: [[name: "*/${params.github_repo_branch}"]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [],
        submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: 'origin', url: "https://github.com/${params.github_org}/${params.github_repo}.git"]]])


        /*sh '''

          ls
          echo "${pwd()}"
          cd env
          ls -la
          echo \$(pwd)
          '''*/


    }

    stage('Validate_Paths')
    {
        echo "=============================================="
        echo "Stage3:Validate paths"
        echo "=============================================="

        //github_repo/env/environment
        sh '''
          #!/bin/bash
          ls && pwd
          '''

            //Defining a string variable to check if the provided environment path exist or not
            def environmentExists = fileExists "${pwd()}/env/${params.environment}"
            //def exists = fileExists '/var/lib/jenkins/workspace/vertical-github-pipeline/env/sandbox/roles/backend.tfvars'
              echo "${pwd()}"
              if (environmentExists)
              {
                  echo "Environment path: ${params.environment} exists."
                  //Defining a string variable to check if the provided github_repo_path  exist or not
                  def githubrepopathExists = fileExists "${pwd()}/env/${params.environment}/${params.github_repo_path }/backend.tfvars"
                  if (githubrepopathExists)
                  {
                      echo "Github repo path: ${params.github_repo_path} exists"
                  }
                  else{
                      echo "Github repo path: ${params.github_repo_path} doesnot exist. Please provide the valid environment"
                      //fail the build
                      currentBuild.result = 'FAILURE'
                      echo "RESULT: ${currentBuild.result}"
                      sh "exit 1"
                  }
              }
              else {
                  echo "Environment path: ${params.environment} doesnot exist. Please provide the valid github_repo_path"
                  //fail the build
                  currentBuild.result = 'FAILURE'
                  echo "RESULT: ${currentBuild.result}"
                  sh "exit 1"
              }
            //echo "${params.github_repo_branch}"
            //sh 'printenv'

    }


  //  This will check the terraform code syntax
   stage('Lint')
   {
        echo "=============================================="
        echo "Stage2:Lint"
        echo "=============================================="
        //terraform validate -var-file=path to env folder on your local/variables.tfvars
  /*  Note: In the git repo iac-iam, if you go to env/sandbox and see contents,you will find roles,vault,backend.tfvars, terraform.tfvars.
      But this code is running on aws server. So, if you connect to server and type pwd, you will get /home/ec2-user.
      This code clones git repo iac-iam in the path /var/lib/jenkins/workspace/vertical-github-pipeline.
      So, if you do cd iac-iam/env/sandbox, it is showing only roles folder which is different from the exact repo in git.
      What is the problem?*/

        echo "${pwd()}"

        dir("${params.github_repo}/env/${params.environment}"){
            echo "testing if it is changing directory"
            echo "${pwd()}"
            echo "Listing contents and running terraform validate command"
            sh 'ls -al'

        }
        dir("${params.github_repo}/env/${params.environment}/${params.github_repo_path}"){
            echo "testing if it is changing directory"
            echo "${pwd()}"
            sh 'ls'
        }
        /*sh """

          echo ${pwd()}
          ls
          cd env/
          echo ${pwd()}
          cd ${params.github_repo}/env/${params.environment}/${params.github_repo_path}
          echo ${pwd()}
          """
          //terraform validate -var-file="${PWD}/env/${params.environment}terraform.tfvars
          //terraform validate -var-file="${PWD}/env/${params.environment}terraform.tfvars -var-file="${PWD}/env/${params.environment}/${params.github_repo_path}/terraform.tfvars "
          //cd ${pwd()}/${params.github_repo}/env/${params.environment}/${params.github_repo_path}*/




    }
    //a compliance stub for future use
    /*stage('Compliance') {
            echo "=============================================="
            println "Stage3:Compliance"
            echo "=============================================="
    }
    //a security stub for future use
    stage('Security') {
            echo "=============================================="
            println "Stage4:Security"
            echo "=============================================="
    }
    //execute the terraform init stage
    stage('Terraform-init') {
            echo "=============================================="
            println "Stage5:Terraform-init"
            echo "=============================================="
    }
    //execute the terraform plan
    stage('Terraform-plan') {
            echo "=============================================="
            println "Stage6:Terraform-plan"
            echo "=============================================="
    }
    //execute the terraform apply
    stage('Terraform-apply') {
            echo "=============================================="
            println "Stage7:Terraform-apply"
            echo "=============================================="
    }
    //produce a report for display regarding the results
    stage('Report'){
            echo "=============================================="
            println "Stage8:Reporting results"
            echo "=============================================="
    }*/
}
