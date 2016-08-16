package double_one_to_one_primary.test;

import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import double_one_to_one_primary.pojo.IdCard;
import double_one_to_one_primary.pojo.Person;

 
/**
 	   基于主键的双向一对一关联
 */
public class TestDouble_one_to_one_primary {

	@Test//创建表结构
	public void testCreateDB(){
		Configuration cfg = new Configuration().configure("double_one_to_one_primary/resources/hibernate.cfg.xml");
		SchemaExport se = new SchemaExport(cfg);
		//第一个参数 是否生成ddl脚本  第二个参数  是否执行到数据库中
		se.create(true, true);
	}

	
	@Test
	public void testSave() throws HibernateException, SerialException, SQLException{
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
		
			IdCard id1 = new IdCard();
			id1.setCode("320123231212112");

			IdCard id2 = new IdCard();
			id2.setCode("110231432423432");
			
			Person person1 = new Person();
			person1.setName("贾宝玉");
			person1.setAge(23);
			person1.setIdCard(id1);
			
			Person person2 = new Person();
			person2.setName("林黛玉");
			person2.setAge(22);
			person2.setIdCard(id2);
			
			Person person3 = new Person();
			person3.setName("薛宝钗");
			person3.setAge(21);
			person3.setIdCard(id2);
			
			session.save(person1);
			session.save(person2);
			//当保存person3出错
			//session.save(person3);
			tx.commit();
			
		}catch (HibernateException e) {
			if(tx!=null)
				tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			HibernateUtil.closeSession();
		}
	}

	
	
	
	
	
	
	@Test
	public void testGet(){
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			System.out.println("------------------------------------");

			
//			Person person = (Person)session.get(Person.class, 1);
//			System.out.println("personName="+person.getName());  //只会查询Persion ,1条语句
//			System.out.println("---------------------------");
//			System.out.println("---IdCard:"+person.getIdCard().getCode());  //会先查询ID卡，在通过ID卡查询Persion，2条语句
//			
//			
			System.out.println("=======================================");
			
			IdCard idCard = (IdCard) session.get(IdCard.class, 1); //查询IDCard,会级联查询出Persion,1条语句
			System.out.println("idCard= "+idCard.getCode());
			System.out.println("-------------");
			System.out.println("personName= "+idCard.getPerson().getName());
			
			
			tx.commit();
		}catch (HibernateException e) {
			if(tx!=null)
				tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			HibernateUtil.closeSession();
		}
	}
}
