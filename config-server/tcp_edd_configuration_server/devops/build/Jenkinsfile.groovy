def IMAGE_TAG = ""
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
        regexpFilterExpression: '^(develop)', 
        regexpFilterText: '$destination_branch', 
        token: 'config-server-merged')
    }
	tools {
        maven 'Maven-3.6.3' 
    }
	parameters {
		  string defaultValue: 'develop', description: 'Branch to be build', name: 'BRANCH_NAME', trim: true
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
		stage('Compile') {
            steps {
				
                script {
				withCredentials([file(credentialsId: 'f68a7ebd-28dc-407d-90a0-47e1e9100cc1', variable: 'MAVEN_SETTING')]) {
							sh '''
							  cat $MAVEN_SETTING >setting.xml
							'''
						  }
                    sh "'mvn' --settings setting.xml clean install -DskipTests"
                }
            }
        }
		stage('Sonar Scan') {
            steps {
				
                script {
					withSonarQubeEnv('sonarqube') {
						sh "'mvn' --settings setting.xml -B clean verify sonar:sonar -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8 -Dspring.profiles.active=ci -Dsonar.junit.reportsPath=target/surefire-reports -Dsonar.jacoco.reportPath=target/jacoco.exec"
					}
                }
            }
        }
		stage('Quality Check') {
            steps {
				script {
						def props = readProperties file: 'target/sonar/report-task.txt'
						def sonarServerUrl=props['serverUrl']
						def ceTaskUrl= props['ceTaskUrl']
						def ceTask
						timeout(time: 2, unit: 'MINUTES') {
							waitUntil {
								sleep(10)
								def response = httpRequest url: ceTaskUrl, authentication: 'sonar_login'
								ceTask = readJSON text: response.content
								return "SUCCESS".equals(ceTask["task"]["status"])
							}
						}
						def response2 = httpRequest url : sonarServerUrl + "/api/qualitygates/project_status?analysisId=" + ceTask["task"]["analysisId"], authentication: 'sonar_login'
						def qualitygate =  readJSON text: response2.content
						if ("ERROR".equals(qualitygate["projectStatus"]["status"])) {
							def msg = "Build Failed Quality Gate!"
							qualitygate["projectStatus"].conditions.each {
									if ("ERROR".equals(it["status"])) {
											msg+= "\nERROR: " + it["metricKey"] + " = " + it["actualValue"] + ", Threshold = " + it["errorThreshold"]
									}
							}
							echo msg
							//error  "Build Failed Quality Gate"
						} else {
							echo "Build Passed Quality Gate!"
						}
				}
            }
        }
		stage('Collect Aritifacts') {
            steps {
                script {
               		sh "rm -rf jars"
					sh "mkdir jars" //empty folder created
					sh "cp target/config-server*.jar jars/." //change name to your jar
					sh "cp -R devops/build/* ."
                }
            }
        }
		stage('Create Docker Images') {
            steps {
                script {
               		SHA = sh(returnStdout: true, script: "git log -n 1 --format=format:%H").trim()
					GITREPO = sh(script: 'echo $(git remote -v | awk \'{print $2}\' | tail -1)',returnStdout: true,)
					NEW_BRANCH_NAME = sh(returnStdout: true, script: 'echo ${BRANCH_NAME} | sed -e \'s/\\//-/g\'').trim()
					def path = env.JOB_NAME.split('/')
					def pathlength = path.length
					def jobName = path[pathlength-2]
					JOB = jobName.toString()
					IMAGE_TAG = NEW_BRANCH_NAME+"-"+BUILD_NUMBER
					sh "\$(aws ecr get-login --no-include-email --region us-west-2)"
					def customImage = docker.build("812436859307.dkr.ecr.us-west-2.amazonaws.com/edd/${JOB}:${NEW_BRANCH_NAME}-${BUILD_NUMBER}", "--build-arg 'build_number=${NEW_BRANCH_NAME}-${BUILD_NUMBER}' --label 'branch=${NEW_BRANCH_NAME}' --label 'sha=${SHA}' --label 'gitrepo=${GITREPO}' --no-cache=true --force-rm --file Dockerfile .")
                    customImage.push()
					
                }
            }
        }
		stage('Trigger Deployment') {
            steps {
                script {
				
				build job: 'services/Deploy/tcp_edd_configuration_server/develop', parameters: [string(name: 'BRANCH_NAME', value: BRANCH_NAME ), string(name: 'ENVIRONMENT', value: 'edd-int4'), string(name: 'IMAGE_TAG', value: IMAGE_TAG)], wait: false
               		
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