def vertical_pipeline = "vertical-github-pipeline"
def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "develop"
def environment = "sandbox"
node{
    echo "${pwd()}"


// Concat or add params and return them


//Make a stage that echo's something


    //pipefuncs = readFile '/var/lib/jenkins/workspace/functions.groovy'
    //pipeline/functions.groovy

    stage("iac_iam_roles"){
        github_repo = "iac-iam"
        github_repo_path = "roles"
        def my_string_var = my_second_function("look at", " me")
        def my_int_var = my_second_function(2, 2)

        //pipefuncs.buildJob(${params.github_org}, ${params.github_repo}, ${params.github_repo_path}, ${params.github_repo_branch}, ${params.environment})
        build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
            ]
 //"${/Users/sainavyapanditi/Documents/repos/pipeline/functions.groovy}"
        //functions.Verticalpipelinejob(github_org,github_repo,github_repo_path,github_repo_branch,environment)
      // def fubnctions = load "${thefilepath}"
      //  functions.executeVerticalPipeline(github_org,ygithub_repo,github_repo_path,wgithub_repo_branch,environment)

    }
    stage("iac-network_base"){
        github_repo = "iac-network"
        github_repo_path = "network-base"

        build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
            ]
    }

}
def my_second_function(param1, param2)
{
    return param1 + param2
    echo "QQQQQQQQQQQQWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWIIIITFUJUFHFHTFJHGKFJRDJTRFKUYGUGJHFJTYFYGJKHGJYG"
}
