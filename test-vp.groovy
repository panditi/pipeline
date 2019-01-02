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


