parallel portalbranch: {
	node {
		env.JAVA_HOME="${tool 'jdk7'}"
		env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
		def mvnHome
		mvnHome = tool 'maven3'
		stage("osivia-portal") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-portal', remote: 'http://www.osivia.org/repos/osivia-portal/branches/4.4-maintenance']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -Punpack,pack -f osivia-portal"
		}
		stage("toutatice-cms") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'toutatice-cms', remote: 'http://projet.toutatice.fr/repos/toutatice-portail/cms/branches/4.4-maintenance']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -Punpack,pack -f toutatice-cms"
		}
		stage("osivia-directory") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-directory', remote: 'http://www.osivia.org/repos/osivia-services/directory/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f osivia-directory"
		}
		stage("osivia-collaboration") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-collaboration', remote: 'http://www.osivia.org/repos/osivia-services/collaboration/branches/4.4-maintenance']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f osivia-collaboration"
		}
//		stage("osivia-procedures") {
//		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-procedures', remote: 'http://www.osivia.org/repos/osivia-services/procedure/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
//		    
//		    sh "'${mvnHome}/bin/mvn' clean install -U -f osivia-procedures"
//		}
//		stage("osivia-tasks") {
//		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'osivia-tasks', remote: 'http://www.osivia.org/repos/osivia-services/tasks/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
//		    
//		    sh "'${mvnHome}/bin/mvn' clean install -U -f osivia-tasks"
//		}
//		stage("cgu") {
//		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'cgu', remote: 'http://www.osivia.org/repos/osivia-services/cgu/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
//		    
//		    sh "'${mvnHome}/bin/mvn' clean install -U -f cgu"
//		}		
		stage("foad-distribution") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'foad', remote: 'http://www.osivia.org/repos/osivia-demo/foad/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f foad -Punpack,pack"
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
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f opentoutatice"
		}
		stage("collab-tools") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'collab-tools', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-collab-tools/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f collab-tools"
		}		
		stage("checkin") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'checkin', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-checkin/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f checkin"
		}
		stage("file-naming") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'file-naming', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-file-based-naming/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f file-naming"
		}
		stage("file-versioning") {
            checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'file-versioning', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-file-versioning/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
            
            sh "'${mvnHome}/bin/mvn' clean install -U -f file-versioning"
        }
		stage("notifications") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'notifications', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-portal-notifications/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f notifications"
		}
		stage("simpleui") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'simpleui', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-simple-ui/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f simpleui"
		}
		stage("es-customizer") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'es-customizer', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-elasticsearch-customizer/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f es-customizer"
		}
		stage("ecm-procedures") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'ecm-procedures', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-procedures/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f ecm-procedures"
		}
		stage("ecm-toutapad") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'ecm-toutapad', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-toutapad/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f ecm-toutapad"
		}
		stage("wf-integration") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'wf-integration', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-workflows-integration/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f wf-integration"
		}
		stage("ottc-news") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'ottc-news', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-news/trunk']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    
		    sh "'${mvnHome}/bin/mvn' clean install -U -f ottc-news"
		}		
		stage("drive") {
			checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'drive', remote: 'http://projet.toutatice.fr/repos/toutatice-ecm/opentoutatice-addons/opentoutatice-drive/branches/4.4-maintenance']], workspaceUpdater: [$class: 'UpdateUpdater']])

			sh "'${mvnHome}/bin/mvn' clean install -U -f drive"
		}
		stage("nx-distrib") {
		    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[credentialsId: '', depthOption: 'infinity', ignoreExternalsOption: true, local: 'nx-distrib', remote: 'http://www.osivia.org/repos/osivia-services/nx-distributions/ottc-collab/branches/4.4-maintenance']], workspaceUpdater: [$class: 'UpdateUpdater']])
		    sh "'${mvnHome}/bin/mvn' clean install -U -f nx-distrib"
		}
	}
}


