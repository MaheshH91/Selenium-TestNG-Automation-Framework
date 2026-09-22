pipeline {
    agent any

  /*  tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }*/

    parameters {
        choice(name: 'BROWSER', choices: ['chrome-headless', 'chrome', 'firefox', 'edge'], description: 'Browser to run tests on')
        choice(name: 'PROFILE', choices: ['Regression', 'Purchase', 'ErrorValidation'], description: 'Maven Profile / Suite to execute')
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
                // Windows batch command uses bat, on Linux/Mac use sh
                bat "mvn clean test -P${params.PROFILE} -Dbrowser=${params.BROWSER}"
            }
        }
    }

    post {
        always {
            // Publish TestNG XML Results
            testNG(reportFilenamePattern: '**/surefire-reports/testng-results.xml')

            // Archive Extent Reports & Test Screenshots
            archiveArtifacts artifacts: 'reports/**/*.html, target/surefire-reports/**', fingerprint: true, allowEmptyArchive: true

            // Publish HTML Extent Report inside Jenkins dashboard
            publishHTML([
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'AutomationReport_*.html',
                reportName: 'Extent Test Report'
            ])
        }
    }
}