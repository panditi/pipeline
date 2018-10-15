node
{
    //defining the parameters
    parameters
    {
        stringParam(defaultValue: '', description: 'User or organization for the github repo', name: 'github_org')
        stringParam(defaultValue: '', description: 'Repo where code is stored', name: 'github_repo')
        stringParam(defaultValue: '', description: 'Root of terraform code inside the githb_repo', name: 'github_repo_path')
        stringParam(defaultValue: '', description: 'Current branch of github_repo', name: 'github_repo_branch')
        stringParam(defaultValue: '', description: 'Environment name of github_repo', name: 'environment')
        stringParam(defaultValue: '', description: 'Name of S3 bucket for terraform state files', name: 's3_bucket')
        stringParam(defaultValue: '', description: 'Key/path to store this enviroment\'s state file in s3; the "name" of the environment', name: 's3_key')


    }
    // This stage checks to make sure the pipeline has been supplied the correct parameters.
    stage('Validation')
    {
        stageHeader(1,'Validation')
        //Clean the workspace
        echo "Cleaning up workspace"
        cleanWs()
        //Dislay the name/value of all parameters
        echo "Github Org: ${params.github_org}"
        echo "Github Repo: ${params.github_repo}"
        echo "Github Repo Path: ${params.github_repo_path}"
        echo "Github branch: ${params.github_repo_branch}"
        echo "Environment: ${params.environment}"
        //check if any parameter is missing
        for(entry in params)
        {
            if(entry.value == null || entry.value.length() == 0)
            {
                println "The parameter missing is: " + entry.key + ". Please provide a value for parameter.."
                //Fail the build if any parameter is missing
                buildFailed()
            }
            else
            {
                println "The parameter provided is: " + entry.key + ". The value of the parameter provided is: " + entry.value
            }
        }

        echo "Done. Validating parameters"
        echo "End of Stage1 : Validation."
        //stageEnd(1,'Validation')

    }

    stage('Checkout SCM')
    {
        stageHeader(2,'Checkout SCM')
        sh 'echo $PWD'
        sh "echo ${env.WORKSPACE}"
        //cloning git repository
        checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${params.github_repo_branch}"]],
                doGenerateSubmoduleConfigurations: false,
                extensions: [
                    [$class: 'CleanBeforeCheckout'],
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "${params.github_repo}"]
                    ],
                submoduleCfg: [],
                userRemoteConfigs: [[
                    credentialsId: 'b74b58be-f128-46ed-8d02-5f7965517a99',
                    url: "https://github.com/${params.github_org}/${params.github_repo}.git"
                    ]]
        ])
        echo "Done. Cloning git repository"
        echo "End of Stage2 : Checkout SCM."

    }
    stage('Validate Paths')
    {
        stageHeader(3,'Validate Paths')

        //github_repo/env/environment
        sh '''
            #!/bin/bash
            ls && pwd
        '''

        //Defining a string variable to check if the provided environment path exist or not
        def environmentExists = fileExists "${pwd()}/${params.github_repo}/env/${params.environment}"
        echo "${pwd()}"
        if (environmentExists)
        {
            echo "Environment path: ${params.environment} exists."
            //Defining a string variable to check if the provided github_repo_path  exist or not
            def githubrepopathExists = fileExists "${pwd()}/${params.github_repo}/env/${params.environment}/${params.github_repo_path}"
            if (githubrepopathExists)
            {
                echo "Github repo path: ${params.github_repo_path} exists"
            }
            else
            {
                echo "Github repo path: ${params.github_repo_path} doesnot exist. Please provide the valid environment"
                //fail the build if github_repo_path doesnot exist
                buildFailed()
            }
        }
        else
        {
            echo "Environment path: ${params.environment} doesnot exist. Please provide the valid github_repo_path"
            //fail the build if environment doesnot exist
            buildFailed()
        }
        echo "Done. Validating paths of ${params.environment} and ${params.github_repo_path}"
        echo "End of Stage3 : Validate Paths."
        dir("${params.github_repo}/env/${params.environment}/${params.github_repo_path}")
        {
            echo "testing if it is changing directory"
            echo "${pwd()}"
            def exists = fileExists 'backend.tfvars'

            if (exists) {
                echo 'Yes backend.tfvars exist'
            } else {
                echo 'No'
            }
        }

    }
    //a compliance stub for future use
    stage('Compliance')
    {
        stageHeader(4,'Compliance')
        echo "In Compliance stage."
    }
    //a security stub for future use
    stage('Security')
    {
        stageHeader(5,'Security')
        echo "In Security stage."
    }
    //execute the terraform init stage
    stage('Terraform init')
    {
        stageHeader(6,'Terraform init')
        echo "${pwd()}"
        dir("${params.github_repo}/env/${params.github_repo_path}")
        {
          echo "${pwd()}"
          sh '''
              ls
              terraform init
          '''
        }
        dir("${params.github_repo}/env/${params.environment}")
        {
            echo "testing if it is changing directory"
            echo "${pwd()}"
            //Remove the terraform state file so we always start from a clean state
            // if (fileExists(".terraform/terraform.tfstate")) {
            // sh "rm -rf .terraform/terraform.tfstate"
            // }
            // sh '''
            //
            // '''
                // ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Linuxbrew/install/master/install)"
                // brew install terraform
                // terraform -version
                // terraform init -backend-config=terraform.tfvars



            /*sh """
                ls
                terraform init -backend-config=../env/"${params.environment}"/backend.tfvars -backend-config=../env/"${params.environment}"/"${params.github_repo_path}"/backend.tfvars
                """*/
        }
    }
  //  This will check the terraform code syntax
   stage('Lint')
   {
        stageHeader(7,'Lint')
        echo "${pwd()}"
        /*dir("${params.github_repo}/env/${params.environment}"){
            echo "testing if it is changing directory"
            echo "${pwd()}"
            echo "Listing contents and running terraform validate command"
            sh 'ls -al'
        }*/
        dir("${params.github_repo}/${params.github_repo_path}")
        {
            echo "testing if it is changing directory"
            echo "${pwd()}"
          /*  sh """
              ls
              terraform validate -var-file=${params.github_repo_path}/terraform.tfvars
            """
        }*/

          //terraform validate -var-file="{params.github_repo_path}/terraform.tfvars"
        }
    }


  /*
    //execute the terraform plan
    stage('Terraform Plan')
    {
        stageHeader(8,'Terraform Plan')
    }
    //execute the terraform apply
    stage('Terraform Apply')
    {
        stageHeader(9,'Terraform Apply')
    }
    //produce a report for display regarding the results
    stage('Report')
    {
        stageHeader(10,'Report')
    }*/
}
//function to add header to each stage
def stageHeader(int stageNumber,String stageName)
{
    echo "=========================================================================================================================================="
    echo "                                                 Stage ${stageNumber} : ${stageName}"
    echo "=========================================================================================================================================="
}
def stageEnd (int stageNumber,String stageName)
{
    echo "END OF STAGE ${stageNumber} : ${stageName}."
}
//function to fail the build whenever reuirements are not met
def buildFailed()
{
    currentBuild.result = 'FAILURE'
    echo "RESULT: ${currentBuild.result}"
    sh "exit 1"
}
