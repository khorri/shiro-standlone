/**
 * 
 */

package com.exmaple.shiro;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;


/**
 * @author k.horri
 * 
 */
public class ShiroBoneCPDataSource extends BoneCPDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = -2248923051363839327L;

    /**
     * 
     */
    public ShiroBoneCPDataSource() {

	super();
	this.setDriverClass("com.mysql.jdbc.Driver");
	this.setJdbcUrl("jdbc:mysql://localhost:3306/shiro");
	this.setUsername("root");
	this.setPassword("root");
	this.setMaxConnectionsPerPartition(10);

    }

    /**
     * @param config
     */
    public ShiroBoneCPDataSource(BoneCPConfig config) {

	super(config);

    }

}
