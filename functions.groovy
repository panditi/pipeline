def buildJob(String github_org, String github_repo, String github_repo_path, String github_repo_branch, String environment){
echo "entered.........................................................................."
def vertical_pipeline = "vertical-github-pipeline"
 build job: vertical_pipeline, parameters: [
     [$class: 'StringParameterValue', name: 'github_org', value: github_org],
     [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
     [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
     [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
     [$class: 'StringParameterValue', name: 'environment', value: environment]
     ]
}
return this;
