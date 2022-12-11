def IMAGE_TAG = ""
def BRANCH_NAME = ""
pipeline {
	 environment {
			environ = "sfcc-int7"
		}
    agent {
        label 'edd_build_slave'
    }
    triggers {
        GenericTrigger (causeString: 'New PR Merged', 
        genericVariables: [
            [defaultValue: '', key: 'destination_branch', 
            regexpFilter: '', 
            value: '$.pullrequest.destination.branch.name'
            ],
            [defaultValue: '', key: 'pr_id',
            regexpFilter: '', value: '$.pullrequest.id'
            ]
        ], 
        regexpFilterExpression: 'release_sfcc_phase1', 
        regexpFilterText: '$destination_branch', 
        token: 'cart-service-merged')
    }
	tools {
        maven 'Maven-3.6.3' 
    }
    options {
        timeout(60)
        timestamps()
    }
    stages {
		stage('Compile') {
            steps {
                script {
				withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
							sh '''
							  cat $MAVEN_SETTING >setting.xml
							'''
						  }
                    sh "'mvn' --settings setting.xml clean install"
                }
            }
        }
		stage('Static Quality'){
            parallel {
                stage('Junit') {
                    steps {
                        script {
                        withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
                                    sh '''
                                    cat $MAVEN_SETTING >setting.xml
                                    '''
                                }
                            sh "'mvn' --settings setting.xml test"
                        }
                    }
                }
                stage('Sonar analysis') {
                    steps {
                    script {
                        withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
                                    sh '''
                                    cat $MAVEN_SETTING >setting.xml
                                    '''
                                }
                        }
                        sh "echo sonar analysis here"
                        withSonarQubeEnv('sonarqube') {
                        sh "'mvn' --settings setting.xml sonar:sonar"
                        }
                    }
                } 
            }
        }
	stage('Sonar scan result check') {
            steps {
			script {
				CURRENT_STAGE=env.STAGE_NAME
				}
                timeout(time: 5, unit: 'MINUTES') {
                    
                        script {
                            def qg = waitForQualityGate()
							if (qg.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure: ${qg.status}"
                            }
							echo "Sonar validation disabled"
                            
                        }
                    
                }
            }
        }
		
		stage('Collect Aritifacts') {
            steps {
                script {
               		sh "rm -rf jars"
					sh "mkdir jars" //empty folder created
					sh "cp target/cart_service*.jar jars/." //change name to your jar
					sh "cp -R devops/build/* ."
                }
            }
        }
		stage('Create Docker Images') {
            steps {
                script {
				    def path = env.JOB_NAME.split('/')
					def pathlength = path.length
					def jobName = path[pathlength-2]
					JOB = jobName.toString()
					def branchName = path[pathlength-1]
					BRANCH_NAME = branchName.toString()
               		SHA = sh(returnStdout: true, script: "git log -n 1 --format=format:%H").trim()
					GITREPO = sh(script: 'echo $(git remote -v | awk \'{print $2}\' | tail -1)',returnStdout: true,)
					NEW_BRANCH_NAME = sh(returnStdout: true, script: 'echo ${BRANCH_NAME} | sed -e \'s/\\//-/g\'').trim()
					
					IMAGE_TAG = NEW_BRANCH_NAME+"-"+BUILD_NUMBER
					sh "\$(aws ecr get-login --no-include-email --region us-west-2)"
					def customImage = docker.build("812436859307.dkr.ecr.us-west-2.amazonaws.com/services/${JOB}:${NEW_BRANCH_NAME}-${BUILD_NUMBER}", "--build-arg 'build_number=${NEW_BRANCH_NAME}-${BUILD_NUMBER}' --label 'branch=${NEW_BRANCH_NAME}' --label 'sha=${SHA}' --label 'gitrepo=${GITREPO}' --no-cache=true --force-rm --file Dockerfile .")
                    customImage.push()
					
                }
            }
        }
		stage('Trigger Deployment') {
            steps {
                script {
				DEPLOY_JOB = 'services/Deploy/tcp_cart_service/'+BRANCH_NAME
				build job: DEPLOY_JOB , parameters: [string(name: 'BRANCH_NAME', value: BRANCH_NAME), string(name: 'ENVIRONMENT', value: environ), string(name: 'IMAGE_TAG', value: IMAGE_TAG)], wait: false
               		
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
            slackSend (channel: '#edd-deployments', color: '#008000', 
			message: "${env.JOB_NAME}: Success\n Image Tag : "+IMAGE_TAG+"\nBuild: [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            echo "Fail"
            slackSend (channel: '#edd-deployments', color: '#FF0000', 
			message: "${env.JOB_NAME}: Fail\n Image Tag : "+IMAGE_TAG+"\nBuild: [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }

    }
}
