# Cooking-recipes

## API Documentation
API docs can be found in the projects [Wiki](https://github.com/Tornadosky/recipes-management/wiki/API-documentation).

## Getting Started with the development

You should have Java version >= 17.
First you need to clone the repository. Then add _.env_ file to _resources_ with db credentials (found in the discord _backend_ channel in the beginning).

To start the application you should run the _RecipesApplication_ class. You should see the following output in the console (important is the absense of errors and the last line 'Started RecipesApplication in ...')

```
(...)
(...)  INFO 8800 --- [ngodb.net:27017] org.mongodb.driver.cluster               : Discovered replica set primary (...)
(...)  INFO 8800 --- [  restartedMain] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with (...)
(...)  INFO 8800 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
(...)  INFO 8800 --- [  restartedMain] dev.gigadev.recipes.RecipesApplication   : Started RecipesApplication in 2.947 seconds (process running for 3.591)
```

## Contributing to the repo

Every time you want to add something new or fix and issue, make sure you follow the steps below:

1. Create new branch from `main` branch by running ```git checkout -b <issue-name>```. This will create a new branch with the `issue-name` and get you to the same branch.

2. Make the necessary changes on your branch and when done, create a `pull request`

NOTE: Also, you may want to update your local(issue) branch from main, to achieve that you need to switch back to the main branch by running `git checkout main`, then you need to pull all the new changes from remote by running `git pull origin main` on your terminal. The next step is to switch back to your issue branch and running `git merge master`. This will update your your issue branch.