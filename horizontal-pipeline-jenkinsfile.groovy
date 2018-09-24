def vertical_pipeline = "vertical-github-pipeline"
def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "master"
def environment = ""

node{
    stage("iac_iam_roles"){
        github_repo = "iac-iam"
        github_repo_path = "roles"

        build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
            ]
    }

  //  stage("iac-network_base"){
   //     github_repo = "iac-network"
       // github_repo_path = "base"

  //      build job: vertical_pipeline, parameters: [
  //          [$class: 'StringParameterValue', name: 'github_org', value: github_org],
  //          [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
  //          [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
  //          [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
  //          [$class: 'StringParameterValue', name: 'environment', value: environment]
  //          ]
  //  }

}
