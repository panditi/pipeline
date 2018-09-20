node {
   // environment {
  //      DISABLE_AUTH = 'true'
   //     DB_ENGINE    = 'sqlite'
  //  }
    // def repositoryUrl = scm.userRemoteConfigs[0].url  
    //def credentialsId = System.getProperty("credentialsId")
    // This checks to make sure the pipeline has been supplied the correct parameters.
    stage('Validation') {
        println "Stage1:Validating"
         //branch name from Jenkins environment variables
  //sh 'printenv'
     echo "My branch is: ${env.BRANCH_NAME}"

     if (env.BRANCH_NAME == 'master') 
        {
   
    //if (GIT_BRANCH == 'master'){
            echo 'I only execute on the master branch'
         
     }
     else {
          echo 'I execute elsewhere'
        }
       checkout scm
       echo "Done checkout scm"
      // scm.getUserRemoteConfigs()[0].getUrl()
        // git branch: '${branch}',
        //      credentialsId: '${credentialsId}'

        //github_org

        //github_repo

        //github_repo_path

        //github_repo_branch

        //aws_account_number

        //aws_account_alias

        //environment
    }
    //This will check the terraform code syntax
    stage('Lint') {
        println "Stage2:Lint"
    }
    //a compliance stub for future use
    stage('Compliance') {
        println "Stage3:Compliance"
    }
    //a security stub for future use
    stage('Security') {
        println "Stage4:Security"
    }
    //execute the terraform init stage
    stage('Terraform-init') {
        println "Stage5:Terraform-init"
    }
    //execute the terraform plan
    stage('Terraform-plan') {
        println "Stage6:Terraform-plan"
    }
    //execute the terraform apply
    stage('Terraform-apply') {
        println "Stage7:Terraform-apply"
    }
    //produce a report for display regarding the results
    stage('Report'){
        println "Stage8:Reporting results"
    }
}


