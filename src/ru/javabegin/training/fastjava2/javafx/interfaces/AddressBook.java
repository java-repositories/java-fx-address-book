package ru.javabegin.training.fastjava2.javafx.interfaces;

import javafx.collections.ObservableList;
import ru.javabegin.training.fastjava2.javafx.objects.Person;

public interface AddressBook {

    boolean add(Person person);

    boolean update(Person person);

    boolean delete(Person person);

    ObservableList<Person> findAll();

    ObservableList<Person> find(String text);

}
