Requirements
============
- To develop a basic personal finances system which shall include:
    - an income and expenditure analysis tool, which will include a system to
      categorise these types of transactions;
    - a budgeting tool, which will use income and expenditure data to produce
      periodic budget estimates.

- to base the system in double entry, I'm gonna use the stuff from this site,
  with some exceptions:
  https://medium.com/@RobertKhou/double-entry-accounting-in-a-relational-database-2b7838a5d7f8


Strategy
--------
- model the backend
  - to build a system trully layered, the database must be able to change. So
    there should be a system which will rebuild a database from a turnkey and
    rebuild it in mysql on a local desktop, upload it to a server and then do
    this.

- then, model the front end -- design a user interface;

- then, check if the backend model still fits, and make necessary adjustments;

- finally, start coding.


Questions to answer
-------------------

### Why develop a personal accounting system?
- because doing personal finance manually would involve too many repetitive
  tasks, and could become too tedious. This is one reasons why the personal
  accounting system \emph{Quicken} was developed: ``Scott Cook was watching his
  wife pay bills and balance the household checkbook at their kitchen table,
  and saw her frustration with how tedious the process was''.
  https://www.quicken.com/about-us


#### What is double entry?
- Double Entry Bookeeping is an accounting system which groups differences
  using pairs of unsigned numbers, which is known in accounting as 'T-accounts'
  [Ellerman 2014].


#### Why is double entry being used, since this is something based on bank statements?
- It will be used as a way to support integrity in the system:
    - it may be useful to have double entry when you want a category be
      considered an asset. For example, if you are paying a mortgage, you can
      have a category of its own called Mortgage, or House, which you can
      classify as an asset. Perhaps you can even create a rule for certain
      entries where for example a percentage of the transaction should be
      considered as interest, and some should be the actual asset value.
    - the system may be expanded to allow some level of flexibility for the
      user: maybe they see a transaction in their bank account which they would
      like to split between two categories, for example, but would also like to
      make sure that any breakdown can still be traced back to the original
      bank statement entry. In this case, a journal composed of `CR Bank 200 -
      ref Purchase1234; DR Category1 100 - Ref Purchase1234; DR Category2 100 -
      ref Purchase1234` would be useful, and a reference could be entered somew

- It will allow more freedom for the user to include entries which may not have
  been included in their bank statement (e.g. if they have cash based sources
  of income and/or expenditure and want to include them in their budget
  estimates).


### What accounting systems are already out there?
- Schutzer, D., Forster Jr, W.H., Hu, H., Lee, W., Stolfo, S.J. and Fan, W.,
  Citibank and NA, 1999. Method and system for using intelligent agents for
  financial transactions, services, accounting, and advice. U.S. Patent
  5,920,848. Retrieved from https://www.google.com/patents/US5920848
  - on page 28, the document states that there are already systems which do
    categorisation of financial transaction, tax information, etc, and that
    they do so by requesting the user to enter this information after the
    transaction has happened. This is similar to what is being developed with
    this system, as the categorisation happens after the information has been
    entered




Brain Dump
----------

- literature review can be just the introduction. See projects book, page 107
  including the example box, and look at the introduction of the sample project
  type 4.

- introduce what an accounting information system is and why we need one.
  - Citation: page 7 of [Boczko,
    2012](https://www.dawsonera.com/readonline/9780273739579)

  - page 8: Vaassen et al. (2009) suggest that the purpose of an accounting
    information system is to provide information for decision making and
    accountability to internal and external stakeholders. Again a common theme
    in each of the above deﬁnitions is the notion that accounting information
    systems possess two common interrelated purposes: 1 to provide users with
    information – or a decision-facilitating function: that is, a function
    concerned with assisting decision making/decision makers by providing
    ‘useful’ infor- mation; and 2 to support decision making and facilitate
    control – or a decision-mediating function: that is, a function concerned
    with controlling and inducing alternative forms of behaviour in transacting
    parties where conflict exists and/or mediation is required. to provide
    users with information There are of course many aspects of
    accounting/ﬁnancial management information – all with their own unique
    deﬁnition of role, purpose and nature – but in general terms, three
    categories can be identiﬁed.


- most of the academic resources seem to be directed at creating an accounting
  system for businesses. Have not yet found any info for a system for
  individual accounting.


- Build the database in a way where the bulk of the data is kept in one
  table,but you have a journal, accounts, etc, all in their own table which
  then will have one-to-many relationship with the bulk data table.


- the application will be simplified as it only keeps track of income and
  expenditure related to your bank account, or credit card statements for
  example.


- create an application layer which is modular.
- might be good to split the application layer as:
  - User interface
  - Business logic
  - database interaction


- competitor software:
  - Xero;
  - Quickbooks;


- test scenarios for trial balance:
  - sum of all values equal zero;
  - sum of all values does not equal zero:
    - data corruption?
    - would this even be allowed to happen, since all interactions will happen
      at application layer level?




