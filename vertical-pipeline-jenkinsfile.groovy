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
            //echo "Before if loop"
            /*if("${params.github_org}" == null || "${params.github_org}".length() == 0){
                echo "enteered first if loop"
                echo "Github_org parameter is provided"
                if("${params.github_repo_branch}" == null || "${params.github_repo_branch}".length() == 0){
                     echo "enteered second if loop"
                    echo "Github_repo_branch parameter is provided"
                }
                else{
                     echo "enteered second else loop"
                    echo "Missing parameter:github_repo_branch"
                }
            }
            else{
                 echo "enteered first else loop"
                echo "Missing parameter:Github_org"
            }






            if("${params.github_org}" == null || "${params.github_org}".length() == 0 ){
                echo "In if loop"
                echo "Missing parameter: Please provide the github_org"
            }
            else{
                echo "In else loop"
                echo "Git hub org Parameter is provided"
            }
            if("${params.github_repo}" == null || "${params.github_repo}".length() == 0 ){
                echo "In if loop"
                echo "Missing parameter: Please provide the github_repo_"
            }
            else{
                echo "In else loop"
                echo "Git hub repo Parameter is provided"
            }

            def myParam = false
            if (params.myParam != null){
                    myParam = params.myParam
            }
            else{
                echo "Missing parameter"
            }
            checkPath(github_repo_path, environment)

           fileExists 'sainavya5/pipeline.git'
             then echo "The file exists in given github repo path: $github_repo_path"*/
            echo "Entering my list"
            //creating list for parameters
            MYLIST = []
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
                                /*sh '''
                                for j in "${MYLIST[element]}"
                                do
                                      echo $j
                                      echo $j >tmp.txt
                                      cat tmp.txt
                                done
                                '''*/
                                echo "The parameter missing is: ${MYLIST[element]}"
                                //currentBuild.result = 'FAILURE'
                                //echo "RESULT: ${currentBuild.result}"

                            }
                            else
                              {
                                //echo "The parameter missing is: ${s}"
                                echo "The parameter validated is: ${MYLIST[element]} "
                                //currentBuild.result = 'SUCCESS'
                            }

                    }

           /* MYTESTLIST = []
                    MYTESTLIST += "params.github_org"
                    MYTESTLIST += "params.github_repo"
                    MYTESTLIST += "github_repo_path"
                    MYTESTLIST += "github_repo_branch"
                    MYTESTLIST += "environment"
                    echo "Before mytestlist"
                    for (def element = 0; element < MYTESTLIST.size(); element++) {
                            //check if each parameter is provided
                            echo "+++entered MYTESTLIST+++"
                           if(MYTESTLIST[element] == null || MYTESTLIST[element].length() ==0)
                           {
                               echo "entered if loop inside mytestlist"
                               sh 'println "The parameter missing is: ${MYTESTLIST[element]} " + MYTESTLIST[element]'
                               //console.log("I would like to visit " + cities[i]);
                           }
                            else
                            {
                                echo "entered else loop inside mytestlist"
                                echo "The parameter validated is: MYTESTLIST[element] "
                            }
                    }
                         //  for (int i = 0; i < list.size(); i++) {
        //sh "echo Hello ${list[i]}"
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


                      //def exists = fileExists "${pwd()}/env/sandbox/roles/backend.tfvars"
//if [ -f "${PWD}/env/${params.environment}/${params.github_repo_path}/backend.tfvars"]

        //def branch = "${params.github_repo_branch}"
        /*echo "Listing contents in current branch"
        sh 'ls'
        echo "Displaying contents of README.md file"
        sh 'cat README.md'*/
        /*sh "cd ~/${params.github_repo} || pwd"
        echo "${pwd()}"
        echo "Changing branch from master to develop"
        echo "${pwd()}"
        sh "git checkout ${params.github_repo_branch}"
          echo "${pwd()}"*/

        /*sh '''
          echo "Printing workspace+"
          echo $PWD
          "echo ${params.github_repo_branch}"
          echo \$(pwd)
          "git checkout ${params.github_repo_branch}"
          echo "Listing the contents in develop branch"
          ls
          echo "${pwd()}"
          cd env
          ls -la
          echo \$(pwd)
          '''

        def source_folder_name = "${pwd()}/${params.environment}"
        //def source_folder = new File(source_folder_name)
        if (!source_folder_name.exists())
        {
           echo "Source folder ${source_folder_name} does not exist"
        }
        else
        {
           echo "Source folder ${source_folder_name} exists"
        }

        //echo "${pwd()}"

        /* dir('iac-iam/env/sandbox/roles/backend.tfvars') {
            echo " In dir block"
            sh 'cat backend.tfvars'
        }
      dir("/${params.github_org}/${params.github_repo}/"){
          echo "entered dir block"
          sh 'ls'
          sh 'pwd'
        }


       echo "Checking if readme.md file exists or not"
       def exists = fileExists 'README.md'

        if (exists) {
            echo 'Yes'
        } else {
            echo 'No'
        }

        echo "outside of second dir"
        dir('iac/jenkins/terraform/common/ecs_slaves/') {
            echo "entered directory block"
            sh 'ls'
            sh 'cat iac/jenkins/terraform/common/ecs_slaves/backend.tf'
        }
        dir('iac/global/terraform_backend/scripts'){
            echo "printing backup-tf-state.sh"
           // sh 'cat backup-tf-state.sh'
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
                  }
              }
              else {
                  echo "Environment path: ${params.environment} doesnot exist. Please provide the valid github_repo_path"
                  //fail the build
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
        sh """

          echo ${pwd()}
          cd /${params.github_repo}/env/${params.environment}/${params.github_repo_path}
          echo ${pwd()}
          """
          //terraform validate -var-file="${PWD}/env/${params.environment}terraform.tfvars
          //terraform validate -var-file="${PWD}/env/${params.environment}terraform.tfvars -var-file="${PWD}/env/${params.environment}/${params.github_repo_path}/terraform.tfvars "
          //cd ${pwd()}/${params.github_repo}/env/${params.environment}/${params.github_repo_path}




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
