# language: en

@web
Feature: Testowanie całego serwisu
  as user want to test whole Segregator application

  @HomePage
  Scenario Outline: Otwieranie strony domowej
    Given go to Home Page
    Then page HomePage visible is true
    Examples:

  Scenario Outline: Otwieranie stron z kanydatami
    Given go to Home Page
    Then page HomePage visible is true
    When go to linked page and click ZarzadzniePozycjami
    Then page ZarzadzniePozycjami visible is true
    Examples:

  Scenario Outline: Zmiana języków
    Given go to Home Page
    Then page HomePage visible is true
    When change language <lang>
    Then language is changed for <lang> is true
    Examples:
      | lang |
      | ch   |
      | en   |
      | de   |
      | pl   |
      | fr   |

  Scenario: Zmiana na prawidłowy język
    Given go to Home Page
    Then page HomePage visible is true
    When change language pl
    But language is changed for en is false

  Scenario Outline: Opisy kategorii
    Given go to Home Page
    Then page HomePage visible is true
    When go to link and click category <name>
    Then garbage category description <name> is True
    Examples:
      | name              |
      | Bio-gastronomia   |
      | PSZOK             |
      | Szkło             |
      | Papier            |
      | Elektrośmieci     |
      | Tworzywa sztuczne |
      | Zielone           |
      | Gabaryty          |
      | Bio               |
      | Zmieszane         |

  Scenario: Zmiana na prawidłowy opis kategorii
    Given go to Home Page
    Then page HomePage visible is true
    When go to link and click category Szkło
    But garbage category description Papier is False

  Scenario Outline: Wprowadzenie nowych pojęć
    Given go to Home Page
    Then page HomePage visible is true
    When search <inputData>
    Then confirmation message is appear
    Examples:
      | inputData |
      | uszka6    |
      | meduza5   |
      | cytryna5  |

  Scenario Outline: Sprawdzenie istniejących pojęć
    Given go to Home Page
    Then page HomePage visible is true
    When search <inputData>
    Then is search data existing
    Examples:
      | inputData     |
      | papier        |
      | brudny papier |

  @Candidate
  Scenario: Dodanie nowego kandydata do bazy
    Given go to Home Page
    Then page HomePage visible is true
    When go to linked page and click ZarzadzniePozycjami
    Then page ZarzadzniePozycjami visible is true
    When open form for candidate uszka6
    Then form opened for candidate uszka6 is true
    When fill form of the candidate with category Zmieszane
    And Save this one candidate
    Then is candidate added

  Scenario: Usunięcie nowego kandydata
    Given go to Home Page
    Then page HomePage visible is true
    When go to linked page and click ZarzadzniePozycjami
    Then page ZarzadzniePozycjami visible is true
    When open form for candidate meduza5
    Then form opened for candidate meduza5 is true
    But form opened for candidate uszka6 is false
    When Delete this one candidate

  Scenario: Resetowanie formularza i wypełnienie danymi
    Given go to Home Page
    Then page HomePage visible is true
    When go to linked page and click ZarzadzniePozycjami
    Then page ZarzadzniePozycjami visible is true
    When open form for candidate cytryna5
    Then form opened for candidate cytryna5 is true
    But form opened for candidate meduza5 is false
      # When reset form for selected candidate
      # Then is form empty
    When fill form data:
      | name        | ostrokrzew6       |
      | category    | Zmieszane         |
      | description | Lorem forum ipsos |
    And Save this one candidate
    Then is candidate added

  Scenario: Otwieranie stron z mapami
    Given go to Home Page
    Then page HomePage visible is true
    When go to link and click category PSZOK
    Then page listaPunktow visible is true
    When go to linked page and click listaPunktow
    Then page CitiFinder visible is true


  Scenario: Sprawdzene filtrowania map
    Given go to Home Page
    Then page HomePage visible is true
    When go to link and click category PSZOK
    Then page listaPunktow visible is true
    When go to linked page and click listaPunktow
    Then page CitiFinder visible is true
    When city search Warszawa
    Then only points of city Warszawa is visible

