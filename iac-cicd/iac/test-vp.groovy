timestamps {
    node("${params.environment}")
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
                                    println "The parameter missing is: " + entry.key + ". Please provide a value for parameter."
                                    //Fail the build if any parameter is missing
                                    buildFailed()
                                }
                                else
                                {
                                    //println ""
                                }
                            }
                            echo "All parameters are provided."

                            echo "Done. Validating parameters"
                            stageEnd(1,'Validation')

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
                                                                credentialsId: 'github_token',
                                                                url: "https://github.com/${params.github_org}/${params.github_repo}.git"
                                                        ]]
                            ])
                            echo "Done. Cloning git repository"
                            stageEnd(2, 'Checkout SCM')
                        }
                stage('Validate Paths')
                        {
                            stageHeader(3,'Validate Paths')
                            //Defining a string variable to check if the provided environment path exist or not
                            def environmentExists = fileExists "${pwd()}/${params.github_repo}/iac/env/${params.environment}"
                            echo "${pwd()}"
                            if (environmentExists)
                            {
                                echo "Environment path: ${params.environment} exists."
                                //Defining a string variable to check if the provided github_repo_path  exist or not
                                def githubrepopathExists = fileExists "${pwd()}/${params.github_repo}/iac/env/${params.environment}/${params.github_repo_path}"
                                if (githubrepopathExists)
                                {
                                    echo "Github repo path: ${params.github_repo_path} exists"
                                }
                                else
                                {
                                    echo "Github repo path: ${params.github_repo_path} doesnot exist. Please provide the valid github repo path paramete.r"
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

                            dir("${params.github_repo}/iac/env/${params.environment}/${params.github_repo_path}")
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
                            echo "Done. Validating paths of ${params.environment} and ${params.github_repo_path}"
                            stageEnd(3, 'Validate Paths')
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
                //Remove the terraform state file so we always start from a clean state
                stage('Terraform init')
                        {
                            stageHeader(6, 'Terraform init')
                            echo "${pwd()}"
                            dir("${params.github_repo}/iac/${params.github_repo_path}")
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
                                        terraform init -backend-config="../env/${params.environment}/backend.tfvars" -backend-config="../env/${params.environment}/${params.github_repo_path}/backend.tfvars"
                                    """
                                    }
                        }
                //execute the terraform plan
                stage('Terraform Plan')
                        {
                            stageHeader(8,'Terraform Plan')
                            echo "${pwd()}"
                            dir("${params.github_repo}/iac/${params.github_repo_path}")
                                    {
                                        echo "testing if it is changing directory"
                                        echo "${pwd()}"
                                        sh """
                                        pwd
                                        ls
                                        which terraform
                                        terraform -version
                                        pwd
                                        terraform plan -var-file="../env/${params.environment}/variables.tfvars" -var-file="../env/${params.environment}/${params.github_repo_path}/variables.tfvars"
                                        """
                                    }
                        }
                //execute the terraform apply
                stage('Terraform Apply')
                        {
                            stageHeader(9,'Terraform Apply')
                            echo "${pwd()}"
                            dir("${params.github_repo}/iac/${params.github_repo_path}")
                                    {
                                        echo "testing if it is changing directory"
                                        echo "${pwd()}"
                                        sh """
                                        pwd
                                        ls
                                        which terraform
                                        terraform -version
                                        pwd
                                        terraform apply -var-file="../env/${params.environment}/variables.tfvars" -var-file="../env/${params.environment}/${params.github_repo_path}/variables.tfvars"
                                         """
                                    }
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
