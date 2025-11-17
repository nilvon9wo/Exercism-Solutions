import monocle.macros.syntax.lens._

import java.time.LocalDate

object LensPerson {
  private type EpochDay = Long
  case class Person(_name: Name, _born: Born, _address: Address)
  case class Name(_foreNames: String, _surName: String)
  case class Born(_bornAt: Address, _bornOn: EpochDay)
  case class Address(_street: String, _houseNumber: Int,
                     _place: String, _country: String)

  val bornStreet: Born => String = _._bornAt._street

  val setCurrentStreet: String => Person => Person = updateAddressStreet
  private def updateAddressStreet(street: String)(person: Person) =
    person.lens(_._address._street)
          .modify(_ => street)

  val setBirthMonth: Int => Person => Person = updateBirthMonth
  private def updateBirthMonth(month: Int) (person: Person)=
    person.lens(_._born._bornOn)
          .modify(calculateEpochDay(month))

  private def calculateEpochDay(month: Int)(birth: Long) =
    LocalDate.ofEpochDay(birth)
             .withMonth(month)
             .toEpochDay

  val renameStreets: (String => String) => Person => Person = updateStreet
  private def updateStreet(function: String => String)(person: Person) =
    person.lens(_._born._bornAt._street)
          .modify(function)
          .lens(_._address._street)
          .modify(function)
}
