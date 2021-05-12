# Test Automation

## Folder Structure

The folder structure for test automation will be as follows:

```
test
├── src
|    └── test
|    |   └──resources
├── .gitignore
├── pom.xml
└── README.md
```

## To run locally

Docker-compose up and then run mvn command from the root

```bash
mvn verify -f test/pom.xml
```

Running the tests create an html report [here](test-output/extent/HtmlReport/ExtentHtml.html)

## Cucumber Tags

We support running the tests with the current tags:

```bash
@frontend
```

### Tags

Using tags will only run the specified tests.

```bash
cd test
```

```bash
mvn verify -Dcucumber.options="--tags '@backend'"
```

```bash
mvn verify -Dcucumber.options="--tags '@frontend'"
```
