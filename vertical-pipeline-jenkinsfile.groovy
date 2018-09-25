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
            echo "Before if loop"
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



            if(("${params.github_org}" == null || "${params.github_org}".length() == 0) &&
            ("${params.github_repo}" == null || "${params.github_repo}".length() == 0) &&
            ("${params.github_repo_path}" == null || "${params.github_repo_path}".length() == 0) &&
            ("${params.github_repo_branch}" == null || "${params.github_repo_branch}".length() == 0) &&
            ("${params.github_repo_path}" == null || "${params.github_repo_path}".length() == 0))
            {
                echo "Missing parameters"
            }
            else
            {
                echo "All parameters are provided"
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
            if("${params.github_repo_path}" == null || "${params.github_repo_path}".length() == 0 ){
                echo "In if loop"
                echo "Missing parameter: Please provide the github_repo_path"
            }
            else{
                echo "In else loop"
                echo "Git hub repo path Parameter is provided"
            }
             if("${params.github_repo_branch}" == null || "${params.github_repo_branch}".length() == 0 ){
                echo "In if loop"
                echo "Missing parameter: Please provide the github_repo_branch"
            }
            else{
                echo "In else loop"
                echo "Git hub repo branch Parameter is provided"
            }

            if("${params.environment}" == null || "${params.environment}".length() == 0 ){
                echo "In if loop"
                echo "Missing parameter: Please provide the environment"
            }
             else{
                echo "In else loop"
                echo "Environment Parameter is provided"
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
                               /*echo "MYLIST"
                                println MYLIST
                                 //printing ELEMENT
                                echo "element"
                                println element
                                 //printing MYLIST[]
                                echo "MYLIST[]"
                                println MYLIST[]
                                 //printing MYLIST[element]
                                echo "MYlist[element]"
                                println MYLIST[element]*/
                                echo "The parameter missing is: ${MYLIST[element]}"
                              }
                            else
                            {
                                echo "The parameter validated is: ${MYLIST[element]} "
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

       checkout([$class: 'GitSCM', branches: [[name: "*/${params.github_repo_branch}"]],
       doGenerateSubmoduleConfigurations: false,
        extensions: [],
        submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: 'origin', url: "https://github.com/${params.github_org}/${params.github_repo}.git"]]])
        //def branch = "${params.github_repo_branch}"
        sh '''
          echo "Listing contents in current branch"
          ls
          echo "Displaying contents of README.md file"
          cat README.md
          pwd
          cd env
          ls -la
          echo \$(pwd)

        '''
        //echo "${pwd()}"



        /*def exists = fileExists "${pwd()}/sandbox"
        if (exists == true) {
            echo "Yes sandbox exists"
        }
        else {
            echo "No sandbox doesn't exist"
        }
        echo "Done"*/




        /* dir('iac-iam/env/sandbox/roles/backend.tfvars') {
            echo " In dir block"
            sh 'cat backend.tfvars'
        }

        /*sh '''if [ -d "iac-iam/env/sandbox/" ]
        then
            echo "Directory /iac-iam/env/sandbox/ exists."
        else
            echo "Error: Directory iac-iam/env/sandbox/ does not exists."
        fi
        '''
      dir("/${params.github_org}/${params.github_repo}/"){
          echo "entered dir block"
          sh 'ls'
          sh 'pwd'
        }
        //echo "Current directory is: "
        //pwd()
       echo "changing to env directory"
        sh 'cd env/'
        echo "Listing the contents in env directory"
        sh 'ls'
        echo "Done with all work"


       echo "Checking if readme.md file exists or not"
       def exists = fileExists 'README.md'

        if (exists) {
            echo 'Yes'
        } else {
            echo 'No'
        }
         sh 'cat README.md'
         echo "Checking if jenkins folder exists or not"
         // Create a File object representing the folder 'A/B'
        //def folder = new File('jenkins')

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
    /*
    stage('Validate_Paths')
    {
        echo "=============================================="
        echo "Stage3:Validate paths"
        echo "=============================================="
        sh 'cd pipeline || ls'

    }


    This will check the terraform code syntax
   stage('Lint') {
            println "=============================================="
            println "Stage2:Lint"

            //sh '''
            //   set +e
            //    echo "testing echo in an sh block that is not assigned to a variable"
            //'''



            println "=============================================="
    }
    //a compliance stub for future use
    stage('Compliance') {
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
