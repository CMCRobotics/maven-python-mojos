Maven BDD Plugin
================

The goal of this plugin is to allow using BDD tools from the Python world
for *integration testing* of Java web applications.
   
Basically, the idea is that you execute
   
  **mvn integration-test**

and it would deploy your Java app to your container and then run integration tests against
it that are written in BDD tools, such as Freshen or Lettuce.
   
How is this different than regular JUnit tests?
-----------------------------------------------

JUnit tests are great, but they require you to write them in Java (which is verbose) and the test cases you
test against are embedded directly in your code. They are impossible to extract by external teams,
such as QA.
   
The idea behind BDD is that you will write the test cases/requirements in a plain text file
using basic English language, e.g.
  
::   
   
	Feature: User REST service
		
	  In order to maintain Users
	  As an Admin/Operations user
	  I want to be able to add, delete and update the list of users in the DB
		
	  Background:
	    Given I am the user Admin with the ADMIN role
	    And there is an existing user called demo
		
	  Scenario: Adding a new user
	    When I add a new user called john_doe
	    Then I should have users Admin, demo and john_doe in the database
		
	  Scenario: Removing a user
	    When I delete the demo user
	    Then I should have users Admin and john_doe in the database
		   

BDD tools allow you map each of these steps into code during integration test runs.

Writing them in Pythony should allow you to be more productive than in regular Java 
and take advantage of the vast Python library ecosystem.
   
Supported tools
---------------

The following BDD tools are supported:

Nose / Freshen 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

https://github.com/rlisagor/freshen

http://somethingaboutorange.com/mrl/projects/nose/

Nose is an extensive unit testing framework for Python. Freshen is a BDD plugin written for that framework.
Hence we support running unit tests writtein in *Nose* in general.

Installation (on Ubuntu):
::
	sudo apt-get install python-setuptools
	sudo easy_install freshen 

Place all your stories and Python test code in

	src/test/python/features

*pom.xml* integration

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>0.2</version>
		<executions>
			<execution>
				<phase>integration-test</phase>
				<goals>
					<goal>nose</goal>
				</goals>
			</execution>
		</executions>
	</plugin>

Lettuce
^^^^^^^

http://lettuce.it/

Lettuce is a stand-alone unit BDD framework for Python.

Installation (on Ubuntu):
::
	sudo apt-get install python-setuptools
	sudo easy_install lettuce 

Place all your stories and Python test code in

	src/test/python/features

*pom.xml* integration

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>0.2</version>
		<executions>
			<execution>
				<phase>integration-test</phase>
				<goals>
					<goal>lettuce</goal>
				</goals>
			</execution>
		</executions>
	</plugin>


Reports
-------

All the BDD reports are created in the
 
	**target/bdd-reports**
 
folder

Goals
-----

Although the recommended solution is hooking up to the **mvn integration-test** phase, you can also run
the BDD goals separately:

* **bdd:nose** - runs all the BDD tests using Nose/Freshen
* **bdd:lettuce** - runs all the BDD tests using Lettuce

Re-running failed tests only
----------------------------

If you are using Nose/Freshen, you can pass the *failed=true* command line option, e.g.

  **mvn integration-test -Dfailed=true**
  
That will append the Nose "--failed" option which will tell it to only re-run the tests that failed during
the last test run.

Maven Repository
----------------

Add the following plugin repository to your *pom.xml* in order to use this plugin:

::

	<pluginRepositories>
		<pluginRepository>
			<id>javabuilders</id>
			<url>http://javabuilders.googlecode.com/svn/repo</url>
		</pluginRepository>
	</pluginRepositories>



