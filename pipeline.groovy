def git_checkout()
{
    stageHeader(2,'Checkout SCM')
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
    sleep 2
    echo "Done. Cloning git repository"
    stageEnd(2, 'Checkout SCM')
}
