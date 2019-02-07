//Calling another function
def github_org = sainavya5
def github_repo = pipeline
def github_repo_branch = master

node('sandbox1){
  stage("Calling function"){
    echo "calling git checkout function present in same file"
    git_checkout()
    echo "After calling git checkout function"
    //echo "calling test function present in different file but in same repo"
    //testFunction()
    //echo "after calling test function"
  }
}
     
def git_checkout()
{
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
}

