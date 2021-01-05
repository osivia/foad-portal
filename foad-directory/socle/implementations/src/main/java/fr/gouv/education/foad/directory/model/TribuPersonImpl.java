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
package fr.gouv.education.foad.directory.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Name;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.urls.Link;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

/**
 * ODM of a person
 * 
 * @author Loïc Billon
 * @since 4.4
 */
@Component("person")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalPerson"})
@Primary
public final class TribuPersonImpl implements TribuPerson {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** DN. */
    @Id
    private Name dn;

    /** CN. */
    @Attribute
    private String cn;

    /** SN. */
    @Attribute
    private String sn;

    /** Display name. */
    @Attribute
    private String displayName;

    /** Given name. */
    @Attribute
    private String givenName;

    /** Email. */
    @Attribute
    private String mail;

    /** Title. */
    @Attribute
    private String title;

    /** UID. */
    @Attribute
    private String uid;

    /** Profiles. */
    @Attribute(name = "portalPersonProfile")
    private List<Name> profiles;

    /** User password. */
    @Attribute
    @Transient
    private String userPassword;

    /** Avatar */
    @Transient
    private Link avatar;

    /** External account indicator. */
    @Attribute(name = "portalPersonExternal")
    private Boolean external;

    /** Account validity date. */
    @Attribute(name = "portalPersonValidity")
    private Date validity;

    /** Creation date. */
    @Attribute(name = "portalPersonCreationDate")
    private Date creationDate;
    
    /** Last connection date. */
    @Attribute(name = "portalPersonLastConnection")
    private Date lastConnection;
    
    /** hash NumEN */
    @Attribute(name = "portalPersonHashNumen")
    private String hashNumen;
        
    /** Fonction */
    @Attribute(name = "portalPersonFonct")
    private String fonction;
    
    /** Fonction administrative */
    @Attribute(name = "portalPersonFonctAdm")
    private String fonctionAdm;
    
    /** RNE */
    @Attribute(name = "portalPersonRne")
    private String rne;
    
    /** RNEs d'exercice */
    @Attribute(name = "portalPersonRneExerc")
    private List<String> rneExerc;
    
    /** Responsables */
    @Attribute(name = "portalPersonRneResp")
    private List<String> rneResp;
    
    /** Nom académie */
    @Attribute(name = "portalPersonNomAca")
    private String nomAcademie;
    
    /** Code académie */
    @Attribute(name = "portalPersonCodeAca")
    private String codeAca;
    
    /** Mail académique */
    @Attribute(name = "portalPersonMailAca")
    private String mailAca;
    /**
     * Constructor.
     */
    public TribuPersonImpl() {
        super();
        this.profiles = new ArrayList<Name>();
        this.avatar = new Link(StringUtils.EMPTY, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Name getDn() {
        return this.dn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDn(Name dn) {
        this.dn = dn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCn() {
        return this.cn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setCn(String cn) {
        this.cn = cn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getSn() {
        return this.sn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setSn(String sn) {
        this.sn = sn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return this.displayName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getGivenName() {
        return this.givenName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMail() {
        return this.mail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getUid() {
        return this.uid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Name> getProfiles() {
        return this.profiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setProfiles(List<Name> profiles) {
        this.profiles = profiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Link getAvatar() {
        return this.avatar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAvatar(Link avatar) {
        this.avatar = avatar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getExternal() {
        return this.external;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setExternal(Boolean external) {
        this.external = external;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date getValidity() {
        return this.validity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidity(Date validity) {
        this.validity = validity;
    }


	/**
     * {@inheritDoc}
     */
    @Override
	public Date getCreationDate() {
		return creationDate;
	}

	/**
     * {@inheritDoc}
     */
    @Override
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	/**
     * {@inheritDoc}
     */
    @Override
    public Date getLastConnection() {
        return this.lastConnection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    
    @Override
	public String getHashNumen() {
		return hashNumen;
	}


	@Override
	public void setHashNumen(String hashNumen) {
		this.hashNumen = hashNumen;
	}


	@Override
	public String getFonction() {
		return fonction;
	}


	@Override
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}


	@Override
	public String getFonctionAdm() {
		return fonctionAdm;
	}


	@Override
	public void setFonctionAdm(String fonctionAdm) {
		this.fonctionAdm = fonctionAdm;
	}


	@Override
	public String getRne() {
		return rne;
	}


	@Override
	public void setRne(String rne) {
		this.rne = rne;
	}


	@Override
	public List<String> getRneExerc() {
		return rneExerc;
	}


	@Override
	public void setRneExerc(List<String> rneExerc) {
		this.rneExerc = rneExerc;
	}


	@Override
	public List<String> getRneResp() {
		return rneResp;
	}


	@Override
	public void setRneResp(List<String> rneResp) {
		this.rneResp = rneResp;
	}


	@Override
	public String getNomAcademie() {
		return nomAcademie;
	}


	@Override
	public void setNomAcademie(String nomAcademie) {
		this.nomAcademie = nomAcademie;
	}


	@Override
	public String getCodeAca() {
		return codeAca;
	}


	@Override
	public void setCodeAca(String codeAca) {
		this.codeAca = codeAca;
	}


	@Override
	public String getMailAca() {
		return mailAca;
	}


	@Override
	public void setMailAca(String mailAca) {
		this.mailAca = mailAca;
	}


	/**
     * {@inheritDoc}
     */
    @Override
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=users").build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Name buildDn(String uid)  {
    	return LdapNameBuilder.newInstance(buildBaseDn()).add("uid=" + uid).build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
