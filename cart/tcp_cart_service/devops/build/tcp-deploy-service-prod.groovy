pipeline {
    agent any
	parameters {
		  string defaultValue: 'develop', description: 'Branch to be build', name: 'BRANCH_NAME', trim: true
          choice choices: ['prod-east-2', 'prod-west-2'], description: 'Environment on which to be deployed', name: 'ENVIRONMENT'
          choice choices: ['us-east-2', 'us-west-2'], description: 'Region on which to be deployed', name: 'REGION'
		  string defaultValue: 'develop-1', description: 'IMAGE Tag which needs to be deployed', name: 'IMAGE_TAG', trim: true
		  choice choices: ['edd', 'edd-stage'], description: 'Region on which to be deployed', name: 'NAMESPACE'
		}
    options {
        timeout(60)
        timestamps()
    }
    stages {
		stage('checkOut') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'a8af1c64-eb7d-410f-9bf0-c372a7ff0663', url: 'https://bitbucket.org/TCP_BitBucket_Admin/tcp_cart_service.git']]])
            }
			
		}
		stage('Deploy-Artifacts') {
            steps {
				
                script {
						sh 'aws eks --region $REGION update-kubeconfig --name $ENVIRONMENT'
						sh "envsubst < devops/k8s/deployment.yaml > temp-deploy.yaml"
						sh "mv temp-deploy.yaml devops/k8s/deployment.yaml"
						sh 'kubectl -n $NAMESPACE apply -f devops/env/configmap/$ENVIRONMENT.yaml'
						sh "kubectl -n $NAMESPACE apply -f devops/k8s/deployment.yaml"
						sh 'kubectl -n $NAMESPACE apply -f devops/env/hpa/$ENVIRONMENT.yaml'
						sh "kubectl -n $NAMESPACE apply -f devops/k8s/service.yaml"
                }
            }
        }
    }

    post {
        always {
            echo 'Clear the workspace'
            deleteDir() /* clean up our workspace */
        }
        success {
            echo "Pass"
        }
        failure {
            echo "Fail"

        }

    }
}