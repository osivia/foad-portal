/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package fr.gouv.education.foad.bns.batch;


import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Remove a user workspace
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RemoveUserProfile  implements INuxeoCommand{


	private String userworkspacePath;

	public RemoveUserProfile(String userworkspacePath) {
		this.userworkspacePath = userworkspacePath;
	}

	/**
	 *
	 * @return 
	 */
	public Object execute(Session nuxeoSession) throws Exception {
	
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        documentService.remove(new PathRef(userworkspacePath));
        
		
		return null;
		
	}

	public String getId() {
		return null;
	}
	

}
