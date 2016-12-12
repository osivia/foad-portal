parallel portalbranch: {
	node {
		env.JAVA_HOME="${tool 'jdk7'}"
		env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
		def mvnHome
		mvnHome = tool 'maven3'
		stage("osivia-portal") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-portal', remote: 'http://www.osivia.org/repos/osivia-portal/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -Punpack,pack -f osivia-portal"
		}
		stage("toutatice-cms") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'toutatice-cms', remote: 'http://projet.toutatice.fr/repos/toutatice-portail/cms/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -Punpack,pack -f toutatice-cms"
		}
		stage("osivia-directory") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-directory', remote: 'http://www.osivia.org/repos/osivia-services/directory/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f osivia-directory"
		}
		stage("osivia-collaboration") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-collaboration', remote: 'http://www.osivia.org/repos/osivia-services/collaboration/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f osivia-collaboration"
		}
		stage("osivia-procedures") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-procedures', remote: 'http://www.osivia.org/repos/osivia-services/procedure/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f osivia-procedures"
		}
		stage("osivia-tasks") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-tasks', remote: 'http://www.osivia.org/repos/osivia-services/tasks/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f osivia-tasks"
		}
		stage("foad-distribution") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'foad', remote: 'http://www.osivia.org/repos/osivia-demo/foad/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f foad -Punpack,pack"
		}
	}
}, nuxeobranch : {
	node {
		env.JAVA_HOME="${tool 'jdk7'}"
		env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
		def mvnHome
		mvnHome = tool 'maven3'
		stage("opentoutatice") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'opentoutatice', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f opentoutatice"
		}
		stage("checkin") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'checkin', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-checkin/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f checkin"
		}
		stage("file-naming") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'file-naming', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-file-based-naming/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f file-naming"
		}
		stage("notifications") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'notifications', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-portal-notifications/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f notifications"
		}
		stage("simpleui") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'simpleui', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-simple-ui/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f simpleui"
		}
		stage("es-customizer") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'es-customizer', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-elasticsearch-customizer/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f es-customizer"
		}
		stage("ecm-procedures") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'ecm-procedures', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-procedures/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f ecm-procedures"
		}
		stage("ecm-toutapad") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'ecm-toutapad', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-toutapad/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -f ecm-toutapad"
		}		
		stage("nx-distrib") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'nx-distrib', remote: 'http://www.osivia.org/repos/osivia-services/nx-distributions/ottc-collab/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    sh "'${mvnHome}/bin/mvn' clean install -f nx-distrib"
		}
	}
}


