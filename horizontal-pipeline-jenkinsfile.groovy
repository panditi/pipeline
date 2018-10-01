//def vertical_pipeline = "vertical-github-pipeline"
def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "develop"
def environment = "sandbox"
def s3_bucket = ""
def s3_key = ""

node{
    echo "${pwd()}"
    stage("iac-iam-roles")
    {
        github_repo = "iac-iam"
        github_repo_path = "roles"
        buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
    }
    stage("iac-network_base")
    {
        github_repo = "iac-network"
        github_repo_path = "network-base"
        buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
    }
    // Send notifications
    //slackSend (channel: '@me', color: colorCode, message: summary)
}
def buildJob(String github_org, String github_repo, String github_repo_path, String github_repo_branch, String environment)
{
    build job: vertical_pipeline, parameters: [
        [$class: 'StringParameterValue', name: 'github_org', value: github_org],
        [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
        [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
        [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
        [$class: 'StringParameterValue', name: 'environment', value: environment]
    ]
}
