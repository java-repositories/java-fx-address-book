package org.example.interfaces;

import javafx.collections.ObservableList;
import org.example.objects.Person;

public interface AddressBook {

    boolean add(Person person);

    boolean update(Person person);

    boolean delete(Person person);

    ObservableList<Person> findAll();

    ObservableList<Person> find(String text);

    /**
     * auxiliary method
     * @return
     */
    ObservableList getPersonList();
}
