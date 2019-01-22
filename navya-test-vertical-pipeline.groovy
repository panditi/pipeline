timestamps {
    node("${params.environment}")
            {
                //defining the parameters
                parameters
                        {
                            stringParam(defaultValue: '', description: 'User or organization for the github repo', name: 'github_org')
                            stringParam(defaultValue: '', description: 'Repo where code is stored', name: 'github_repo')
                            stringParam(defaultValue: '', description: 'Path of terraform code inside the githb_repo', name: 'github_repo_path')
                            stringParam(defaultValue: '', description: 'Folder of terraform code inside the githb_repo', name: 'tolder')
                            stringParam(defaultValue: '', description: 'Current branch of github_repo', name: 'github_repo_branch')
                            stringParam(defaultValue: '', description: 'Environment name of github_repo', name: 'environment')
                            stringParam(defaultValue: '', description: 'Name of S3 bucket for terraform state files', name: 's3_bucket')
                            stringParam(defaultValue: '', description: 'Key/path to store this enviroment\'s state file in s3; the "name" of the environment', name: 's3_key')

                        }
                currentBuild.displayName = "${params.environment}-${params.github_repo}-${params.github_repo_path}-#${currentBuild.number}"
                // This stage checks to make sure the pipeline has been supplied the correct parameters.
                stage('Validation')
                        {
                            stageHeader(1,'Validation')
                            validateParams()
                            stageEnd(1,'Validation')
                        }

                stage('Checkout SCM')
                        {
                            stageHeader(2,'Checkout SCM')
                            checkoutScm()
                            stageEnd(2, 'Checkout SCM')
                        }
                stage('Validate Paths')
                        {
                            stageHeader(3,'Validate Paths')
                            validatePaths()
                            stageEnd(3, 'Validate Paths')
                        }
                //a compliance stub for future use
                stage('Compliance')
                        {
                            stageHeader(4,'Compliance')
                            compliance()
                            stageEnd(4, 'Compliance')
                        }
                //a security stub for future use
                stage('Security')
                        {
                            stageHeader(5,'Security')
                            security()
                            stageEnd(5,'Security')
                        }
                //execute the terraform init stage
                //Remove the terraform state file so we always start from a clean state
                stage('Terraform init')
                        {
                            stageHeader(6, 'Terraform init')
                            tf_init()
                            stageEnd(6, 'Terraform init')
                        }
                stage('Terraform Validate')
                        {
                            stageHeader(7, 'Terraform Validate')
                            tf_validate()
                            stageEnd(7, 'Terraform Validate')
                        }
                //execute the terraform plan
                stage('Terraform Plan')
                        {
                            stageHeader(8,'Terraform Plan')
                            tf_plan()
                            stageEnd(8, 'Terraform Plan')
                        }
                //execute the terraform apply
                stage('Terraform Apply')
                        {
                            stageHeader(9,'Terraform Apply')
                            tf_apply()
                            stageEnd(9, 'Terraform Apply')
                        }
            }
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
def validateParams()
{
    //Dislay the name/value of all parameters
    echo "Github Org: ${params.github_org}"
    echo "Github Repo: ${params.github_repo}"
    echo "Github Repo Path: ${params.github_repo_path}"
    echo "Github branch: ${params.github_repo_branch}"
    echo "Environment: ${params.environment}"
    //check if any parameter is missing
    def fail_param_validation = false
    for(entry in params)
    {
        if(entry.value == null || entry.value.length() == 0)
        {
            println "The parameter missing is: " + entry.key + ". Please provide a value for parameter."
            //Fail the build if any parameter is missing
            buildFailed()
        }
    }
    echo "All parameters are provided."
}
def checkoutScm()
{
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
                                        credentialsId: 'github_token',
                                        url: "https://github.com/${params.github_org}/${params.github_repo}.git"
                                ]]
    ])
    echo "Cloning git repository success"
}
def validatePaths()
{
    echo "${pwd()}"
    //Defining a string variable to check if the provided environment path exist or not
    def environmentExists = fileExists "${pwd()}/${params.github_repo}/iac/env/${params.environment}"
    echo "${pwd()}"
    if (environmentExists)
    {
        echo "Environment path: ${params.environment} exists."
        //Defining a string variable to check if the provided github_repo_path  exist or not
        def githubrepopathExists = fileExists "${pwd()}/${params.github_repo}/iac/env/${params.environment}/${params.tolder}"
        if (githubrepopathExists)
        {
            echo "Github repo path: ${params.github_repo_path} exists"
            def exists = fileExists 'backend.tfvars'

            if (exists) {
                echo "Yes backend.tfvars file exist in the provided ${tolder}"
            }
            else {
                echo 'No backend.tfvars file do not exist in the provided ${tolder}'
            }
        }
        else
        {
            echo "Github repo path: ${params.github_repo_path} doesnot exist. Please provide the valid github repo path parameter."
            //fail the build if github_repo_path doesnot exist
            buildFailed()
        }
    }
    else
    {
        echo "Environment path: ${params.environment} doesnot exist. Please provide the valid environment parameter."
        //fail the build if environment doesnot exist
        buildFailed()
    }

    echo "Done. Validating paths of ${params.environment} and ${params.github_repo_path}"
}
def compliance()
{
    echo "In stage compliance"
}
def security()
{
    echo "In stage security"
}
def tf_init()
{
    echo "${pwd()}"
    dir("${params.github_repo}/${params.github_repo_path}")
            {
                echo "${pwd()}"
                sh """
                     pwd
                     ls
                     terraform -version
                     which terraform
                     if [ -e .terraform/terraform.tfstate ]
                          then
                          echo "File exist."
                          rm -rf .terraform/terraform.tfstate
                          echo "Removed .terraform file so that we can always start from a clean state"
                     else
                          echo ".terraform/terraform.tfstate File doesnot exist"
                          fi
                          pwd
                          terraform init -backend-config="../env/${params.environment}/backend.tfvars" -backend-config="../env/${params.environment}/${params.tolder}/backend.tfvars"
                """
            }
}
def tf_validate()
{
    echo "${pwd()}"
    dir("${params.github_repo}/${params.github_repo_path}")
            {
                echo "${pwd()}"
                sh """
                     pwd
                     ls
                     terraform -version
                     which terraform
                     terraform validate -var-file="../env/${params.environment}/variables.tfvars" -var-file="../env/${params.environment}/${params.tolder}/variables.tfvars"
                """
            }
}
def tf_plan()
{
    echo "Checking which account"
    sh 'aws sts get-caller-identity'
    echo "${pwd()}"
    dir("${params.github_repo}/${params.github_repo_path}")
            {
                echo "testing if it is changing directory"
                echo "${pwd()}"
                sh """
                    pwd
                    ls
                    which terraform
                    terraform -version
                    pwd
                    terraform plan -var-file="../env/${params.environment}/variables.tfvars" -var-file="../env/${params.environment}/${params.tolder}/variables.tfvars"
                """
            }
}

def tf_apply()
{
    echo "${pwd()}"
    dir("${params.github_repo}/${params.github_repo_path}")
            {
                echo "testing if it is changing directory"
                echo "${pwd()}"
                sh """
                    pwd
                    ls
                    which terraform
                    terraform -version
                    pwd
                    terraform apply -var-file="../env/${params.environment}/variables.tfvars" -var-file="../env/${params.environment}/${params.tolder}/variables.tfvars"
                """
            }
}
