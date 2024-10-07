package ru.javabegin.training.fastjava2.javafx.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import ru.javabegin.training.fastjava2.javafx.interfaces.AddressBook;
import ru.javabegin.training.fastjava2.javafx.objects.Person;

public class HibernateAddressBook implements AddressBook {

    private ObservableList<Person> personList = FXCollections.observableArrayList();

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public HibernateAddressBook() {
        findAll();
        sessionFactory = getSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    @Override
    public boolean add(Person person) {
        Session session = sessionFactory.openSession();
        session.save(person);
        session.flush();
        session.close();
        return true;
    }

    @Override
    public boolean update(Person person) {
        Session session = sessionFactory.openSession();
        session.saveOrUpdate(person);
        session.flush();
        session.close();
        return true;
    }

    @Override
    public boolean delete(Person person) {
        Session session = sessionFactory.openSession();
        session.delete(person);
        session.flush();
        session.close();
        return true;
    }

    @Override
    public ObservableList<Person> findAll() {
        personList.clear();
        Session session = getSessionFactory().openSession();
        personList.addAll(session.createCriteria(Person.class).list());
        session.close();
        return personList;
    }

    @Override
    public ObservableList<Person> find(String text) {
        personList.clear();

        Session session = getSessionFactory().openSession();


        Criterion rest1 = Restrictions.like("fio", text + "%");
        Criterion rest2 = Restrictions.like("phone", text + "%");

        personList.addAll(session.createCriteria(Person.class).add(Restrictions.or(rest1, rest2)).list());
        session.close();
        return personList;
    }


    public ObservableList<Person> getPersonList() {
        return personList;
    }
}
