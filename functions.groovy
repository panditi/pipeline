
def buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment){

  build job: vertical_pipeline, parameters: [
      [$class: 'StringParameterValue', name: 'github_org', value: github_org],
      [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
      [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
      [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
      [$class: 'StringParameterValue', name: 'environment', value: environment]
      ]

}

// executeVerticalPipeline
