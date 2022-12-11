 pipeline {
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
        regexpFilterExpression: '^(release_sfcc_phase1)', 
        regexpFilterText: '$destination_branch', 
        token: 'core-framework-merged')
    }

	tools {
        maven 'Maven-3.6.3' 
    }
	parameters {
		  string defaultValue: 'release_sfcc_phase1', description: 'Branch to be build', name: 'BRANCH_NAME', trim: true
		}
    options {
        timeout(60)
        timestamps()
    }
    stages {
		stage('checkOut') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'a8af1c64-eb7d-410f-9bf0-c372a7ff0663', url: 'https://bitbucket.org/TCP_BitBucket_Admin/tcp_core_framework.git']]])
            }
			
		}

        stage('Compile') {
            steps {
                script {
				withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
							sh '''
							  cat $MAVEN_SETTING >setting.xml
							'''
						  }
                    sh "'mvn' --settings setting.xml clean compile"
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

		stage('Deploy') {
            steps {
				
                script {
				withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
							sh '''
							  cat $MAVEN_SETTING >setting.xml
							'''
						  }
                    sh "'mvn' --settings setting.xml deploy -DskipTests"
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
			message: "${env.JOB_NAME}: Success\nBuild: [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            echo "Fail"
            slackSend (channel: '#edd-deployments', color: '#FF0000', 
			message: "${env.JOB_NAME}: Fail\n Build: [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }

    }
}