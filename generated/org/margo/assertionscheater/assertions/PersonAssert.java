package org.margo.assertionscheater.assertions;

import static org.assertj.core.api.Assertions.fail;

import java.lang.String;
import org.margo.assertionscheater.Objects.Address;
import org.margo.assertionscheater.Objects.Person;

public final class PersonAssert {
  private final Person actual;

  private PersonAssert(Person actual) {
    this.actual = actual;
  }

  PersonAssert assertThat(Person actual) {
    return new PersonAssert(actual);
  }

  public PersonAssert hasFirstName(String firstName) {
    if(!(actual.getFirstName().equals(firstName))) {
      fail("Unexpected firstName. Expected: " + firstName + " but was: " + actual.getFirstName() + " .");
    }
    return this;
  }

  public PersonAssert hasLastName(String lastName) {
    if(!(actual.getLastName().equals(lastName))) {
      fail("Unexpected lastName. Expected: " + lastName + " but was: " + actual.getLastName() + " .");
    }
    return this;
  }

  public PersonAssert hasAge(int age) {
    if(!(actual.getAge() == age)) {
      fail("Unexpected age. Expected: " + age + " but was: " + actual.getAge() + " .");
    }
    return this;
  }

  public PersonAssert hasAddress(Address address) {
    if(!(actual.getAddress().equals(address))) {
      fail("Unexpected address. Expected: " + address + " but was: " + actual.getAddress() + " .");
    }
    return this;
  }
}
