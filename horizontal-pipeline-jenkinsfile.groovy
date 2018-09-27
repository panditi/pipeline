def vertical_pipeline = "vertical-github-pipeline"
def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "develop"
def environment = "sandbox"

node{
    stage("iac_iam_roles"){
        github_repo = "iiac-iam"
        github_repo_path = "roles"

 //"${/Users/sainavyapanditi/Documents/repos/pipeline/functions.groovy}"
        //functions.Verticalpipelinejob(github_org,github_repo,github_repo_path,github_repo_branch,environment)
      // def fubnctions = load "${thefilepath}"
      //  functions.executeVerticalPipeline(github_org,ygithub_repo,github_repo_path,wgithub_repo_branch,environment)
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
