pipeline {
    agent any

    /* tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    } */

    parameters {
        choice(name: 'BROWSER', choices: ['chrome-headless', 'chrome', 'firefox', 'edge'], description: 'Browser target to run tests on')
        choice(name: 'TEST_SUITE', choices: ['Regression', 'Purchase', 'ErrorValidation'], description: 'Maven Profile / Suite to execute')
        choice(name: 'ENVIRONMENT', choices: ['QA', 'Stage', 'Dev'], description: 'Target test execution environment')
    }

    stages {
        stage('Checkout Code') {
            steps {
                cleanWs()
                checkout scm
            }
        }

        stage('Execute Tests') {
            steps {
                script {
                    def mavenCmd = "mvn clean test -P${params.TEST_SUITE} -Dbrowser=${params.BROWSER} -Denv=${params.ENVIRONMENT}"
                    if (isUnix()) {
                        sh mavenCmd
                    } else {
                        bat mavenCmd
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish TestNG XML Results
            testNG(reportFilenamePattern: '**/surefire-reports/testng-results.xml')

            // Archive Extent Reports, TestNG reports & screenshots
            archiveArtifacts artifacts: 'reports/**/*.html, target/surefire-reports/**', fingerprint: true, allowEmptyArchive: true

            // Publish HTML Extent Report in Jenkins Dashboard
            publishHTML([
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExecutionReport_*.html',
                reportName: 'Extent Test Report'
            ])

            // Email Notification with report attachments
            emailext(
                to: 'holkarmahesh@outlook.com',
                subject: "[Jenkins] ${currentBuild.currentResult}: Job '${env.JOB_NAME}' (Build #${env.BUILD_NUMBER})",
                body: """
                    <h2>Build Status: ${currentBuild.currentResult}</h2>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                    <p><b>Test Profile:</b> ${params.TEST_SUITE}</p>
                    <p><b>Environment:</b> ${params.ENVIRONMENT}</p>
                    <p><b>Browser:</b> ${params.BROWSER}</p>
                    <hr/>
                    <p><b>Console Log:</b> <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>
                    <p><b>Extent Report Dashboard:</b> <a href="${env.BUILD_URL}Extent_20Test_20Report/">View Extent Dashboard</a></p>
                """,
                mimeType: 'text/html',
                attachmentsPattern: 'reports/ExecutionReport_*.html, target/surefire-reports/emailable-report.html',
                recipientProviders: [developers(), requestor()]
            )
        }

        success {
            echo "All tests executed and passed under profile: '${params.TEST_SUITE}'"
        }

        failure {
            echo "Build failed. Inspect ExtentReport, TestNG reports, or console logs for details."
        }
    }
}