package org.margo.assertionscheater.assertions;

import static org.assertj.core.api.Assertions.fail;

import java.lang.String;
import org.margo.assertionscheater.Objects.Address;

public final class AddressAssert {
  private final Address actual;

  private AddressAssert(Address actual) {
    this.actual = actual;
  }

  AddressAssert assertThat(Address actual) {
    return new AddressAssert(actual);
  }

  public AddressAssert hasCountry(String country) {
    if(!(actual.getCountry().equals(country))) {
      fail("Unexpected country. Expected: " + country + " but was: " + actual.getCountry() + " .");
    }
    return this;
  }

  public AddressAssert hasCity(String city) {
    if(!(actual.getCity().equals(city))) {
      fail("Unexpected city. Expected: " + city + " but was: " + actual.getCity() + " .");
    }
    return this;
  }

  public AddressAssert hasZipCode(int zipCode) {
    if(!(actual.getZipCode() == zipCode)) {
      fail("Unexpected zipCode. Expected: " + zipCode + " but was: " + actual.getZipCode() + " .");
    }
    return this;
  }

  public AddressAssert hasStreet(String street) {
    if(!(actual.getStreet().equals(street))) {
      fail("Unexpected street. Expected: " + street + " but was: " + actual.getStreet() + " .");
    }
    return this;
  }

  public AddressAssert hasBuilding(int building) {
    if(!(actual.getBuilding() == building)) {
      fail("Unexpected building. Expected: " + building + " but was: " + actual.getBuilding() + " .");
    }
    return this;
  }
}
