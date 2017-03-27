package com.rahul;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author v043731
 */
public class IpmrLDAPTest {

    public LdapContext getLdapContext() {
        LdapContext ctx = null;
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            // OU=IT,DC=vcn,DC=ds,DC=volvo,DC=net
            env.put(Context.SECURITY_PRINCIPAL, "CN=cs-ws-s-CONDOR-TEST,OU=Service Accounts,OU=WINSRV,OU=CS,DC=vcn,DC=ds,DC=volvo,DC=net");
            env.put(Context.SECURITY_CREDENTIALS, "MG78Hdc-Me98%Kf");
            env.put(Context.PROVIDER_URL, "ldap://vcn.ds.volvo.net:389");
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful.");
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        System.out.println("Connected to AD: " + ctx.toString());
        return ctx;
    }

    public NamingEnumeration getUserBasicAttributes(String username, LdapContext ctx) {
        NamingEnumeration answer = null;
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrIDs = { "distinguishedName", "sn", "givenname", "mail", "telephonenumber" };
            constraints.setReturningAttributes(attrIDs);
            answer = ctx.search("DC=vcn,DC=ds,DC=volvo,DC=net", "cn=" + username, constraints);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return answer;
    }

    public void displayLdapInfo(NamingEnumeration answer) {
        try {
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println(attrs.get("distinguishedName"));
                System.out.println(attrs.get("givenname"));
                System.out.println(attrs.get("sn"));
                System.out.println(attrs.get("mail"));
                System.out.println(attrs.get("telephonenumber"));
            } else {
                System.out.println("Invalid User");
            }
        } catch (NamingException ex) {
            System.out.println("GenericLdapUtilTest error" + ex.toString());
        }

    }
}
