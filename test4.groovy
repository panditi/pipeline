def git_checkout()
{
    //cloning git repository
    checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${github_repo_branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [
                    [$class: 'CleanBeforeCheckout'],
                    [$class: 'RelativeTargetDirectory', relativeTargetDir: "${github_repo}"]
            ],
            submoduleCfg: [],
            userRemoteConfigs: [[
                                        credentialsId: 'github_token',
                                        url: "https://github.com/${github_org}/${github_repo}.git"
                                ]]
    ])
    echo "Done. Cloning git repository"
}
def testFunction()
{
    echo "Successfully calling function in diffrent file"
}
