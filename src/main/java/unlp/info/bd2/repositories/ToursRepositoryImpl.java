package unlp.info.bd2.repositories;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import unlp.info.bd2.model.User;

public class ToursRepositoryImpl implements ToursRepository{

    @Autowired LocalSessionFactoryBean sessionFactoryBean;

    public void save(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.persist(o);
        session.getTransaction().commit();
        session.close();
    }

}
