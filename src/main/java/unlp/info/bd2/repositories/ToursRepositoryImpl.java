package unlp.info.bd2.repositories;

import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import unlp.info.bd2.model.User;

public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    LocalSessionFactoryBean sessionFactoryBean;

    public void save(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.persist(o);
        session.getTransaction().commit();
        session.close();
    }

    public <T> Optional<T> findById(long id, Class<T> resultClass) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        Optional<T> object = session.createQuery(
                String.format("from %s where id = %d", resultClass.getName(), id), resultClass).uniqueResultOptional();
        session.getTransaction().commit();
        return object;
    }

    public void update(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.merge(o);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.remove(o);
        session.getTransaction().begin();
        session.close();
    }

    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        Optional<User> user = session
                .createQuery(String.format("from User where username = '%s'", username), User.class)
                .uniqueResultOptional();
        session.getTransaction().commit();
        return user;
    }
}
