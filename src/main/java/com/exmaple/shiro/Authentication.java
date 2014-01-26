package com.exmaple.shiro;

import javax.print.attribute.standard.RequestingUserName;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Authentication {
	private static Logger log = Logger.getLogger(Authentication.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Authentication auth = new Authentication();
		String login = "admin";
		char[] pass = { 'a', 'd', 'm', 'i', 'n' };
		Subject subject = auth.authenticateWithShiro(login, pass);
		if (subject != null && subject.isAuthenticated()) {
			System.out.println("successfull login");
		} else {
			System.out.println("Failed log in");
		}
		auth.write();

	}

	/**
	 * 
	 * @param username
	 * @param pass
	 */
	private Subject authenticateWithShiro(String username, char[] pass) {

		Subject currentUser = null;
		try {
			log.info("Authentication with shiro is started...");

			Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
					"classpath:shiro.ini");
			org.apache.shiro.mgt.SecurityManager securityManager = factory
					.getInstance();
			// the key "jdbcRealm" must be the same in the shiro.ini file.
			JdbcRealm realm = (JdbcRealm) ((IniSecurityManagerFactory) factory)
					.getBeans().get("jdbcRealm");
			realm.setAuthenticationQuery("SELECT password FROM users WHERE login=?");
			realm.setUserRolesQuery("SELECT role.name FROM role,users_roles,users WHERE role.roleId=users_roles.roleId AND users_roles.userId=users.userId AND users.login=?");
			realm.setPermissionsQuery("SELECT permission FROM role_permission,role WHERE role_permission.roleId=role.roleId AND role.name=?");
			realm.setPermissionsLookupEnabled(true);
			SecurityUtils.setSecurityManager(securityManager);

			currentUser = SecurityUtils.getSubject();

			Session session = currentUser.getSession();
			session.setAttribute("someKey", "aValue");
			String value = (String) session.getAttribute("someKey");
			if (value.equals("aValue")) {
				log.info("Retrieved the correct value! [" + value + "]");
			}

			// let's login the current user so we can check against roles and
			// permissions:
			if (!currentUser.isAuthenticated()) {
				UsernamePasswordToken token = new UsernamePasswordToken(
						username, pass);
				token.setRememberMe(true);
				currentUser.login(token);

			}
		} catch (Exception e) {
			log.error("Authentication failed", e);
		}
		return currentUser;

	}
	public void write(){
		Subject subject= SecurityUtils.getSubject();
		if(subject.hasRole("Administrator")){
			log.info("has role administrator");
		}else{
			log.info("has no role");
		}
		if(subject.isPermitted("admin:write")){
			log.info("Administrator is permitted to write");
		}else{
			log.info("Administrator is NOT permitted to write");
		}
	}
}
