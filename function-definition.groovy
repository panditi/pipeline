def stageBegin(String stageName) {
    echo "\033[32m==========================================================================================================================================\033[0m"
    echo "\033[32m                                                 STAGE: ${stageName}\033[0m"
    echo "\033[32m==========================================================================================================================================\033[0m"
}
def stageEnd(String stageName) {
    echo " "
    echo "\033[32m END OF STAGE: ${stageName}\033[0m"
    echo " "
    echo " "
}
def validateParams(allowed_to_be_empty) {
    echo 'INFO:  validating all job params'
    echo "INFO:  allowed_to_be_empty|${allowed_to_be_empty}|"

    def errors = []
    for (entry in params) {
        echo "INFO:  Checking param |${entry.key}|"
        if (!allowed_to_be_empty.contains(entry.key)) {
            if (entry.value == null) {
                println "ERROR:  The parameter missing is: |${entry.key}|.  Please provide a value for parameter."
                errors << entry.key
            }
        }
    }
    echo "INFO: test 1"
    if (errors) {
        buildFailed("Missing values for parameters: |${errors}|")
        return
    } else {
        echo 'INFO:  SUCCESS: All parameters are provided.'
    }
    echo "INFO:  end of validate"
}
def buildFailed(msg) {
    currentBuild.result = 'FAILURE'
    echo "ERROR:  BUILD FAILED,  ${msg}"
    sh 'exit 1'
}

return this

