def defaults = [:]
defaults['github_org'] = 'opploans'
defaults['github_repo'] = 'iac-iam'
defaults['github_repo_path'] = 'roles'
defaults['github_repo_branch'] = 'develop'
defaults['environment'] = 'sandbox'


def github_org = params.gitub_org == null ? defaults['opploans'] : params.github_org,
    github_repo = params.github_repo == null ? defaults['iac-iam'] : params.github_repo,
    github_repo_path = params.gitub_repo_path == null ? defaults['roles'] : params.github_repo_path,
    github_repo_branch = params.github_repo_branch == null ? defaults['develop'] : params.github_repo_branch,
    environment = params.environment == null ? defaults['sandbox'] : params.environment,

properties([
    [
        $class: 'ParametersDefinitionProperty',
        parameterDefinitions: [
            [
                $class      : 'StringParameterDefinition',
                name        : 'github_org',
                defaultValue: opploans
                required    : true
            ],
            [
                $class      : 'StringParameterDefinition',
                name        : 'github_repo',
                defaultValue: iac-iam,
                required    : true
            ],
            [
                $class      : 'StringParameterDefinition',
                name        : 'github_repo_path',
                defaultValue: roles,
                required    : true
            ],
            [
                $class      : 'StringParameterDefinition',
                name        : 'github_repo_branch',
                defaultValue: develop,
                required    : true
            ],
            [
                $class      : 'StringParameterDefinition',
                name        : 'environment',
                defaultValue: sandbox,
                required    : true
            ],
        ]
    ]
])

assert github_org.trim() != "" : "github_org not defined"
assert github_repo.trim() != "" : "github_repo not defined"
assert github_repo_path.trim() != "" : "github_repo_path not defined"
assert github_repo_branch.trim() != "" : "github_repo_branch not defined"
assert environment.trim() != "" : "environment not defined"
node{
    //defining the parameters
  /*  parameters{
            stringParam(defaultValue: '', description: '', name: 'github_org')
            stringParam(defaultValue: '', description: '', name: 'github_repo')
            stringParam(defaultValue: '', description: '', name: 'github_repo_path')
            stringParam(defaultValue: '', description: '', name: 'github_repo_branch')
            stringParam(defaultValue: '', description: '', name: 'environment')
            //choice(choices: ['master', 'develop'], description: '', name: 'github_repo_branch')
             }
*/
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
          /*  MYLIST = []
                    MYLIST += "${params.github_org}"
                    MYLIST += "${params.github_repo}"
                    MYLIST += "${params.github_repo_path}"
                    MYLIST += "${params.github_repo_branch}"
                    MYLIST += "${params.environment}"

                    for (def element = 0; element < MYLIST.size(); element++) {
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
          '''

       echo "Checking if readme.md file exists or not"
       def exists = fileExists 'README.md'

        if (exists) {
            echo 'Yes'
        } else {
            echo 'No'
        }

        echo "outside of  dir"
        dir('iac/jenkins/terraform/common/ecs_slaves/') {
            echo "entered directory block"
            sh 'ls'
            sh 'cat iac/jenkins/terraform/common/ecs_slaves/backend.tf'
        }*/


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
        dir("${params.github_repo}/env/${params.environment}/${params.github_repo_path}"){
            echo "printing backup-tf-state.sh"
            sh 'ls'

           // sh 'cat backup-tf-state.sh'
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
