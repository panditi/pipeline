node(){
definition {
              cps {
                script readFileFromWorkspace('pipeline_function.groovy') + workflow
                  }
            }
stage("Checkout")
{
echo "Checkout from git"
checkoutScm()
}
