def vertical_pipeline = "vertical-github-pipeline"
def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def github_repo_branch = "develop"
def environment = "sandbox"



node{
    echo "${pwd()}"
    // executeVerticalPipeline
    stage ('Build') {
        try {
            notifyBuild('STARTED')
        }
        catch (e) {
            // If there was an exception thrown, the build failed
            currentBuild.result = "FAILED"
            throw e
        }
        finally {
            // Success or failure, always send notifications
            notifyBuild(currentBuild.result)
        }
    }
    stage("iac_iam_roles"){
        github_repo = "iac-iam"
        github_repo_path = "roles"
        //buildJob(github_org, github_repo, github_repo_path, github_repo_branch, environment)
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
  /*  stage("iac-network_base"){
        github_repo = "iac-network"
        github_repo_path = "network-base"

        build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
            ]
    }*/

}
def notifyBuild(String buildStatus = 'STARTED') {
    // build status of null means successful
    buildStatus =  buildStatus ?: 'SUCCESSFUL'

    // Default values
    def colorName = 'RED'
    def colorCode = '#FF0000'
    def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"
    def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

    // Override default values based on build status
    if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESSFUL') {
        color = 'GREEN'
        colorCode = '#00FF00'
    } else {
        color = 'RED'
        colorCode = '#FF0000'
    }

    // Send notifications
    slackSend (channel: '@me', color: colorCode, message: summary)
}
/*def buildJob(String github_org, String github_repo, String github_repo_path, String github_repo_branch, String environment){

 build job: vertical_pipeline, parameters: [
     [$class: 'StringParameterValue', name: 'github_org', value: github_org],
     [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
     [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
     [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
     [$class: 'StringParameterValue', name: 'environment', value: environment]
     ]
}*/
