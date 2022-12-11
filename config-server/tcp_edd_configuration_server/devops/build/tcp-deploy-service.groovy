pipeline {
    agent {
        label 'edd_build_slave'
    }
	parameters {
		  string defaultValue: 'develop', description: 'Branch to be build', name: 'BRANCH_NAME', trim: true
		  string defaultValue: 'edd-int4', description: 'Environment on which to be deployed', name: 'ENVIRONMENT', trim: true
		  string defaultValue: 'develop-1', description: 'IMAGE Tag which needs to be deployed', name: 'IMAGE_TAG', trim: true
		  string defaultValue: 'us-west-2', description: 'REGION Tag which needs to be deployed', name: 'REGION', trim: true
		  string defaultValue: 'np-west-2', description: 'CLUSTER Tag which needs to be deployed', name: 'CLUSTER', trim: true
		}
    options {
        timeout(60)
        timestamps()
    }
    stages {
		stage('checkOut') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'a8af1c64-eb7d-410f-9bf0-c372a7ff0663', url: 'https://bitbucket.org/TCP_BitBucket_Admin/tcp_edd_configuration_server.git']]])
            }
			
		}
		stage('Deploy-Artifacts') {
            steps {
				
                script {
						sh 'aws eks --region $REGION update-kubeconfig --name $CLUSTER'
						sh "envsubst < devops/k8s/deployment.yaml > temp-deploy.yaml"
						sh "mv temp-deploy.yaml devops/k8s/deployment.yaml"
						sh 'kubectl -n $ENVIRONMENT apply -f devops/env/configmap/$ENVIRONMENT.yaml'
						sh "kubectl -n $ENVIRONMENT apply -f devops/k8s/deployment.yaml"
						sh 'kubectl -n $ENVIRONMENT apply -f devops/env/hpa/$ENVIRONMENT.yaml'
						sh "kubectl -n $ENVIRONMENT apply -f devops/k8s/service.yaml"
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