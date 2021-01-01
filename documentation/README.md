# Documentation

## Build

In order to build all the components, just run the following command.

```shell
mvn package
```

If you want to run the applications as containers even locally or on Kubernetes, a [Google Jib](https://github.com/GoogleContainerTools/jib) configuration is available for building the corresponding Docker images.

```shell
mvn package jib:dockerBuild
```

The above command will build the Docker images locally; you have to push them to the registry you prefer manually.