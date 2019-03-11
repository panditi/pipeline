#!/usr/bin/env groovy

properties([
        disableConcurrentBuilds(),
        buildDiscarder(logRotator(daysToKeepStr: '5', numToKeepStr: '10')),
])

def odin_clojure_build_job      = 'odin-build-clojure-lib'
def String[] params_allow_empty = []

def odin_environment            = 'test1'
def aws_account                 = 'service'
def lib_name                    = 'chatty-tests'
def aws_region                  = 'us-east-1'
def github_lib_org              = 'opploans'
def github_lib_repo             = 'chatty-tests'
def github_lib_branch           = 'master'
def lib_artifact_bucket         = "${github_lib_org}-${aws_account}-artifacts"
def lib_artifact_path           = "lib"
def java_home                   = '/usr/lib/jvm/java-8-openjdk-amd64/jre/bin'
def github_token                = "odin-jenkins-build"
def github_checkout_org         = 'opploans'
def github_checkout_repo        = 'odin-cicd'
def github_checkout_branch      = 'master'
ansiColor('xterm') {
    timestamps {
        node(odin_environment) {

            // ==========================================================================================

            def stage_name           = ''
            int stage_no             = 0
            def odin_build_info      = "${odin_environment}:${lib_name}:${github_lib_branch}"
            currentBuild.displayName = "${odin_build_info}-#${currentBuild.number}"
            checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${github_checkout_branch}"]],
                doGenerateSubmoduleConfigurations: false,
                extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'RelativeTargetDirectory', relativeTargetDir: "${github_checkout_repo}"]
                ],
                submoduleCfg: [],
                userRemoteConfigs: [[
                                            credentialsId: "${github_token}",
                                            url: "https://github.com/${github_checkout_org}/${github_checkout_repo}.git"
                                    ]]
        ])

            sh 'pwd'
            def rootDir= pwd()
            println("Current Directory: " + rootDir)
            // point to exact source file
            def example = load "${rootDir}/odin-cicd/iac/jenkins-as-code/pipeline/pipeline-horizontal"
            sh """
            pwd
            ls -al
           
            """
            echo "calling all functions present in different file but in same repo"
            example.stageBegin()
            example.stageEnd()
            example.validateParams(params_allow_empty)
            example.buildFailed()
            echo "after calling test function"

            stage_name = "${++stage_no}. DISPLAY JOB INFO for ${odin_build_info}"
            stage (stage_name) {
                stageBegin(stage_name)
                def message ="""\033[32m

        JOB VARIABLES VALUES:
        ======================================================================================================

                            aws_account: ${aws_account}
                       odin_environment: ${odin_environment}
                               lib_name: ${lib_name}
                             aws_region: ${aws_region}
                         github_lib_org: ${github_lib_org}
                        github_lib_repo: ${github_lib_repo}
                      github_lib_branch: ${github_lib_branch}
                    lib_artifact_bucket: ${lib_artifact_bucket}
                      lib_artifact_path: ${lib_artifact_path}

        OTHER JENKINS JOB VARIABLES:
        ======================================================================================================
                             
                              java_home: ${java_home}
                 odin_clojure_build_job: ${odin_clojure_build_job}
                     params_allow_empty: ${params_allow_empty}


        JENKINS ENVIRONMENT VALUES:
        ======================================================================================================

                     BUILD_DISPLAY_NAME: ${env.BUILD_DISPLAY_NAME}
                               BUILD_ID: ${env.BUILD_ID}
                           BUILD_NUMBER: ${env.BUILD_NUMBER}
                              BUILD_TAG: ${env.BUILD_TAG}
                              BUILD_URL: ${env.BUILD_URL}
                        EXECUTOR_NUMBER: ${env.EXECUTOR_NUMBER}
                           JENKINS_HOME: ${env.JENKINS_HOME}
                            JENKINS_URL: ${env.JENKINS_URL}
                          JOB_BASE_NAME: ${env.JOB_BASE_NAME}
                               JOB_NAME: ${env.JOB_NAME}
                                JOB_URL: ${env.JOB_URL}
                            NODE_LABELS: ${env.NODE_LABELS}
                              NODE_NAME: ${env.NODE_NAME}
                              WORKSPACE: ${env.WORKSPACE}

        ======================================================================================================

        \033[0m"""
                echo "${message}"
                stageEnd(stage_name)
            }

            stage_name = "${++stage_no}. VALIDATE JOB PARAMS"
            stage (stage_name) {
                stageBegin(stage_name)
                validateParams(params_allow_empty)
                stageEnd(stage_name)
            }

            stage_name = "${++stage_no}. BUILD AND DEPLOY CLOJURE LIB"
            stage (stage_name) {
                stageBegin(stage_name)
                stageEnd(stage_name)
            }

        } // node
    } // timestamps
} // ansiColor
