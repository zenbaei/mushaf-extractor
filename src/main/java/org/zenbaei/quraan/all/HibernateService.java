package org.zenbaei.quraan.all;

import java.util.Properties;
import java.util.function.Consumer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateService {
	
	public static SessionFactory sessionFactory = null;  
	private static ServiceRegistry serviceRegistry = null;  
	  
	public static SessionFactory configureSessionFactory() throws HibernateException {  
	    Configuration configuration = new Configuration();  
	    configuration.configure();  
	    
	    Properties properties = configuration.getProperties();
	    
		serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();          
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
	    
	    return sessionFactory;  
	}
	
	public static void tx(Consumer<Session> consumer){
		Session session = null;
		Transaction tx=null;
		
		try {
			session = HibernateService.sessionFactory.openSession();
			tx = session.beginTransaction();
			
			consumer.accept(session);
			
			// Committing the change in the database.
			session.flush();
			tx.commit();
				
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally{
			if(session != null) {
				session.close();
			}
		}
			
	}
}
