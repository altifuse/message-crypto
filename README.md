# Message Crypto [![Build Status](https://travis-ci.org/arturhgca/message-crypto.svg?branch=master)](https://travis-ci.org/arturhgca/message-crypto)

Datablink Message Crypto is a web application developed to demonstrate my workflow, my programming abilities and, most importantly, my learning capacities.

It offers to registered users an interface to save, read and decrypt a secret message that only they can access.

## Check it out!

A test deploy is currently available at [Heroku](https://boiling-savannah-18733.herokuapp.com). It may take a while to load at first; don't worry, this is because the free tier of Heroku puts all apps to sleep after 30 minutes of inactivity.

## How to use?

This is a self-contained web application. Therefore, after building, you only need to run the jar file and enter the application through your favorite web browser at https://{your_ip_or_hostname}:8443/.

### Prerequisites

To build this project from source, you will need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) + [JCE Unlimited Strength Jurisdiction Policy Files 8](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
- [Maven](https://maven.apache.org/)
- [MySQL](https://www.mysql.com/)

### Installing & Running

#### Preparing the database

In your MySQL server, run the following commands:

```mysql
create database datablink_message_crypto;
create user 'dbmc_user'@'localhost' identified by 'dbmc_password'; -- If you wish to change these credentials, remember to change them in /src/main/resources/application.properties as well
grant all on datablink_message_crypto.* to 'dbmc_user'@'localhost';
```

#### Enabling 256-bit encryption

This application uses AES-256 to secure user messages. Because of US cryptography export laws, JRE only has native support for up to 128-bit encryption. In order to use longer keys, the Java Cryptography Extension files are needed. The package contains detailed installation instructions. **Important:** the files should be placed inside the `jre/lib/security` directory in the `jdk` path.

**Note for OpenJDK users:** you probably don't need the JCE files. Run the following command:

```shell
jrunscript -e 'print (javax.crypto.Cipher.getMaxAllowedKeyLength("AES") >= 256);'
```

If it returns `true`, you are ready to go.

#### Running the application

After you clone the repository, run the following command in the project root:

`mvn clean install spring-boot:run`

This will remove any previous builds, create a uber-jar (that is, a jar file which contains all needed dependencies), and run it.

**About SSL:** the source includes a locally-generated certificate for testing purposes. For deployment, you may (and *should*) replace it with a real certificate and reflect this change in  `src/main/resources/application.properties`. A free option is [Let's Encrypt](https://letsencrypt.org/), an organization backed by the Linux Foundation, Mozilla, and others.

## Development

### Architecture

![](https://raw.githubusercontent.com/arturhgca/message-crypto/master/docs/architectural_diagram.png)

### Milestones

- ~~Create base project~~
- ~~Create basic HTML templates~~
- ~~Create corresponding controllers~~
- ~~Implement persistence~~
- ~~Secure credentials~~
- ~~Implement message encryption/decryption~~
- ~~Implement input sanitization~~
- **Improve documentation** ◀
- ~~Treat exceptions~~
- ~~Improve UI/UX~~
- ~~Implement unit tests~~
- Implement stateless session management

## Technologies

The project uses the following technologies (so far):

- Dependency manager: **[Maven](https://maven.apache.org/)**
- Framework: **[Spring](https://projects.spring.io/spring-framework/)** - specifically [Spring Boot](https://projects.spring.io/spring-boot/) for its ease of use
- Template engine: **[Thymeleaf](http://www.thymeleaf.org/)** for being HTML-based
- IDE: **[IntelliJ IDEA](https://www.jetbrains.com/idea/)**, because I'm used to and like PyCharm
- Persistence: **[MySQL](https://www.mysql.com/)**
- CSS framework: [**Materialize**](http://materializecss.com/) with [jQuery](https://jquery.com/)
- Testing: [**JUnit**](http://junit.org/junit4/) with [Mockito](http://site.mockito.org/) and [Hamcrest](http://hamcrest.org/)

## Challenges

- ~~Which template engine to use?~~ There isn't a big reasoning behind choosing Thymeleaf over something like Freemarker, I just chose something to learn about

- ~~How to manage sessions RESTfully?~~ Sessions should be handled statelessly, by tokens; one option is [JWT](https://jwt.io/). Spring Security already implements CSRF protection by using session tokens, albeit not JSON-based ones

- ~~How to store user credentials?~~ The [Spring Security](https://projects.spring.io/spring-security/) module offers password encryption with BCrypt

- ~~How to encrypt user messages?~~ AES256 seems to be the way to go. Problems to be solved in this case:
  - ~~What to use as key?~~ A key is derived from the user's password with BCrypt
  - ~~Where to store the key? If this key is generated upon successful login (by deriving the key at the same time the password is hashed in order to verify identity), where should it be stored for the current session?~~ When the user logs in, their credentials are stored in memory and, every time they save a message, a new encryption key is generated and its salt is stored in the database alongside the encrypted message
  - ~~What to use as IV? Randomly generate an IV for each new message and store it~~ Since a new encryption key is used for each message, there is no need for an IV; nonetheless, it is used
  - ~~How to actually handle the encryption/decryption?~~ Spring Security has this covered, but it needs [Oracle JCE](https://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters/6481658#6481658) for the app to run because of JRE limitations

- ~~How to prevent SQL injections/XSS?~~ Parameterized queries for SQL and input sanitization, such as HTML escaping.
  - With the Spring Boot JPA implementation, the simple database access this application needs doesn't need custom queries. Because of this, there is no need to worry about SQL injections
  - About XSS: Thymeleaf already HTML escapes all output unless it's in a `th:utext` (unescaped) field

- ~~How to correctly handle exceptions in a Spring Web application?~~ By default, Spring Boot handles all exceptions by redirecting the user to the existing `/error` views. This is an easy solution and sufficient from the point of view of an end user, but it has a flaw that affects both developers and applications that may rely on a REST API: it does not forward *what* happened. On the other hand, letting the user know what kind of error was thrown may not be a good idea from a security perspective. For this case, I'm still not sure which is the best approach. Basically, the options are:

  - Leave the error handling as-is, that is, set the error templates up and let Spring deal with them;
  - Disable Spring's own error mappings and handle exceptions manually.

  Currently, I'm using a mix of these two approaches: errors directly related to user experience are handled manually (e.g. user/password/message length), while internal errors are handled by Spring.

- ~~How to create unit tests for a Spring Web MVC application?~~ Unit tests for a web controller, for instance, should be able to mock a request and verify the correct handling of resources, such as validation and mapping. Spring Boot has a [base dependency](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) available that includes not only their own testing library for Spring applications but also many other tools such as [Mockito](http://mockito.org/) and [Hamcrest](http://hamcrest.org/JavaHamcrest/).

- ~~Should the architecture include a Service layer?~~ When studying about unit testing in Spring, I understood that a separation of business logic from the controllers would make maintaining and testing the code easier. But I don't think such a change would be valid at the current stage of the project (testing and finalizing). Nevertheless, it's something I've learned.


