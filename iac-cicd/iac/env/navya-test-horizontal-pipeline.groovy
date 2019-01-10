def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "master"
def environment = "sandbox1"
def s3_bucket = ""
def s3_key = ""

timestamps {
    node("${environment}"){
        echo "${pwd()}"
        stage("iac-iam-roles")
                {
                    github_repo = "iac-iam"
                    github_repo_path = "roles"
                    //calling buildJob function to trigger job
                    buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
                }
        stage("iac-iam-kms")
                {
                    github_repo = "iac-iam"
                    github_repo_path = "kms"
                    //calling buildJob function to trigger job
                    buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
                }
        stage("iac-network-network-base")
                {
                    github_repo = "iac-network"
                    github_repo_path = "network-base"
                    buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
                }
    }
}
//triggering vertical pipeline job
def buildJob(String github_org, String github_repo, String github_repo_path, String github_repo_branch, String environment)
{
    def vertical_pipeline = 'navya-test-vertical-pipeline'
    build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
    ]
}
