function Verticalpipelinejob{
  //FUNCTION TO BUILD VERTICAL PIPELINE job
  build job: vertical_pipeline, parameters: [
      [$class: 'StringParameterValue', name: 'github_org', value: github_org],
      [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
      [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
      [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
      [$class: 'StringParameterValue', name: 'environment', value: environment]
      ]

}



// executeVerticalPipeline
