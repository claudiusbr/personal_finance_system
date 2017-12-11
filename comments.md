Log
===

- I started this log on the 9th of December, so all entries prior to this date
  should be seen as summaries of what I remember was the outcome of my work on
  or around each date.

- I may use both first and third person point of view while writing this, so
  the log should be viewed as an informal piece of writing. My intention when
  writing it was to use it as an outlet for my thoughts when faced with
  difficult decisions, to create in some level a registry of my thought
  process, and as tool to help analyse my actual progress based on the
  timelines provided in the project proposal.


26/Nov/2017
-----------
- literature search and review:
    - Boczko, Tony. Introduction to Accounting Information Systems. Harlow,
      England: Pearson.
      - [found at](https://www.dawsonera.com/readonline/9780273739579)
    - [attributed to michaelwigley@bcs.org.uk](http://web.archive.org/web/20131013080026/http://homepages.tcp.co.uk/~m-wigley/gc_wp_ded.html)
      - the same material has been replicated several times under different sources:
        - [Vik Patel, 2012](https://vikrampareek.wordpress.com/2012/09/19/185/)
        - [Robert Khou, 2016](https://medium.com/@RobertKhou/double-entry-accounting-in-a-relational-database-2b7838a5d7f8)
    - Ellerman, D., 2014. On double-entry bookkeeping: The mathematical treatment. Accounting Education, 23(5), pp.483-501. 
      - [for bibtex](https://scholar.google.co.uk/scholar?hl=en&as_sdt=0%2C5&sciodt=0%2C5&cites=14007546902740213321&scipsc=&q=On+double-entry+bookkeeping%3A+The+mathematical+treatment&btnG=)

2/Dec/2017
----------
- still busy with literature review of the accounting sources.
- made a decision to proceed with the decision of creating a double entry
  system, even after analysing the comments on Michael Wigley's article that a
  single entry system may be better suited to a system where the entries all
  derive from a single source -- which, in the case of this system, would
  consist on a user's bank statements. The decision was based on the fact that,
  although yes, the entries are likely to come from a bank statement alone, it
  would be useful to treat each category as a ledger, so that you could perform
  other operations on each of them, and can always track them down to an
  original entry on the bank statement.


8/Dec/2017
----------
- decided that if possible I would like to build a system which can be
  available on several platforms, so as to provide for a scenario as such:
  - the user may decide to keep the whole system in a USB (I belive this is
    called a turnkey?) which would allow them to run on any machine. So the
    application and database (possibly sqlite?) would have to be self
    contained, and either leave the data encrypted, or expect that the user
    themselves will encrypt the usb or whatever device they are storing the
    program in.
  - Then maybe after a while they realise that most of the time when they are
    working on their finances it tends to be on their personal computer, so
    they can download a desktop client for it which may use a different
    database (mysql?). 
  - Further down the line, their lifestyle may change in a way where they no
    longer wish to store any personal data locally -- all of their data is kept
    in some cloud based storage, and they only use web-based applications. So,
    to keep up with their new lifestyle, the program should have a hosted
    option which keeps their data on a server and provides access to it via a
    web-based interface.

- all of the above should be done as user friendly as possible, in a way which
  as much as possible the complexity would be hidden away from the user.


9/Dec/2017
----------
- referring to the decision from 2nd/Dec to proceed with double entry, I have
  now decided that it may also be useful to have an interface to allow the user
  to make manual entries to their income or expenditure. This would allow a
  more accurate estimate of their analysis and budgeting, and it also
  reinforces the argument that double entry would be beneficial

- finished the use case diagram and created an activity diagram to illustrate
  how the categorisation of entries should happen.

- as part of the literature search and review, I came across a patent for a
  system which seems to integrate personal finance systems directly with
  financial service providers
  ([source](https://www.google.com/patents/US5920848)). Page 28 of the patent
  document seems to indicate that the system provides some level of financial
  advice based on the data entered and transactions executed within the system
  itself.
  

11/Dec/2017
-----------
- decided on a strategy to develop the system.
  - model the backend
    - to build a system which conforms to the decision made on the 8th of
      December, the database must be able to change. So there should be a
      system which will retrieve a database from a turnkey and rebuild it in
      mysql on a local desktop, upload it to a server and then do this.

  - then, model the front end -- design a user interface;

  - then, check if the backend model still fits, and make necessary adjustments;

  - finally, start coding.
