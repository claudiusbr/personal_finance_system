Title: The design and build of a simple personal finance system,
================================================================
focused on budgeting and analysing expenditure
==============================================

Objectives
----------
To develop a basic personal finances system which shall include:
- an income and expenditure analysis tool, which will include a system to
  categorise these types of transactions;
- a budgeting tool, which will use income and expenditure data to produce
  periodic budget estimates.  
  
Time permitting, the features below will also be included:
- a personal tax estimator;
- a feature which analyses entered categories for income and expenditure data,
  and uses it to suggest categories for any new entries;
- a web server which will store categories and patterns related to them to
  attempt to improve the accuracy of the category suggestion tool.

Description
-----------
Design a simple application to assist with personal finances. The system will
be based on the accounting principle of double entry. The project will include
a description of this system, and what are its advantages and disadvantages. It
will try to implement this using databases and an application layer, by
restricting the operations allowed on the tables holding the income and
expenditure data to ‘Read’ and ‘Insert’, with some limited access to ‘Create’.
This restriction may be implemented within the database itself and/or at the
application layer level.

The application will be hosted in the user’s machine, and will allow the user
to upload bank and credit card statements in a suitable format (initially CSV,
but time permitting the program will be adapted to accept other input types).
It will then ask the user for category names for each entry, and for a pattern
to match that category. This pattern will be later used to match future entries
and try to suggest a category for them.  

The interface implemented shall allow the user to: 
- view and export income and expenditure reports by period;
- generate a budget based on that data; and
- save the budget for comparison with future income and expenditure.

Time permitting, a feature will also be implemented to allow the user to
estimate whether they have over or under­paid taxes. This will be an estimate
only, and should be used as a guideline to help the user decide if they should
take a closer look at their taxes.



Method
------
This project will be developed over a few iterations, following the approach
below:
- Define the requirements;
- Analyse them;
- Design a solution;
- Implement and test it.

The first few iterations should be spent on elaboration and should result on an
initial minimum viable product, so during these more time should be spent on
analysis and design. Whereas the last few iterations will be more focused on
construction, so should see more time dedicated to design and implementation of
the solution.

Test Driven Development shall be used throughout the implementation phase to
facilitate testing, and where appropriate design patterns will be used. Also,
as much as possible the classes and entities shall adhere to SOLID principles.

Git will be used for version control.
