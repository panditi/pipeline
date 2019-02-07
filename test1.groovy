//Calling another function

node('sandbox1){
  stage("Calling function"){
    echo "before calling function"
    testFunction()
    echo "after calling function"
  }
}
