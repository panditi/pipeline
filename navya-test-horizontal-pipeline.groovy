def github_org = "opploans"
def github_repo = ""
def github_repo_path = ""
def tolder = ""
def github_repo_branch = "master"
def environment = "sandbox1"
def s3_bucket = ""
def s3_key = ""

timestamps {
    node("${environment}"){
        echo "${pwd()}"
        currentBuild.displayName = "${environment}-#${currentBuild.number}"
        // stage("odin-iam-iac-roles")
        //         {
        //             github_repo = "odin-iam"
        //             tolder = "roles"
        //             github_repo_path = "iac/roles"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        stage("odin-iam-iac-kms")
                {
                    github_repo = "odin-iam"
                    tolder = "kms"
                    github_repo_path = "iac/kms"
                    //calling buildJob function to trigger job
                    buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
                }
        // stage("odin-network-iac-network-base")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "network-base"
        //             github_repo_path = "iac/network-base"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-dns")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "dns"
        //             github_repo_path = "iac/dns"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-ssm")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "ssm"
        //             github_repo_path = "iac/ssm"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-vpc-peering")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "vpc-peering"
        //             github_repo_path = "iac/vpc-peering"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-interface-vpc-endpoints")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "interface-vpc-endpoints"
        //             github_repo_path = "iac/interface-vpc-endpoints"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-sg")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "sg"
        //             github_repo_path = "iac/sg"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
        // stage("odin-network-iac-sg-aux")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "sg-aux"
        //             github_repo_path = "iac/sg-aux"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }

        // stage("odin-network-iac-interface-vpc-endpoints-aux")
        //         {
        //             github_repo = "odin-network"
        //             tolder = "interface-vpc-endpoints-aux"
        //             github_repo_path = "iac/interface-vpc-endpoints-aux"
        //             //calling buildJob function to trigger job
        //             buildJob(github_org, github_repo, github_repo_path, tolder, github_repo_branch, environment)
        //         }
    }
}
//triggering vertical pipeline job
def buildJob(String github_org, String github_repo, String github_repo_path, String tolder, String github_repo_branch, String environment)
{
    def vertical_pipeline = 'navya-test-vertical-pipeline'
    build job: vertical_pipeline, parameters: [
            [$class: 'StringParameterValue', name: 'github_org', value: github_org],
            [$class: 'StringParameterValue', name: 'github_repo', value: github_repo],
            [$class: 'StringParameterValue', name: 'github_repo_path', value: github_repo_path],
            [$class: 'StringParameterValue', name: 'tolder', value: tolder],
            [$class: 'StringParameterValue', name: 'github_repo_branch', value: github_repo_branch],
            [$class: 'StringParameterValue', name: 'environment', value: environment]
    ]
}
