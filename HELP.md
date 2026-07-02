# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/web/servlet.html)
* [Validation](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/io/validation.html)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/web/spring-security.html)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/web/spring-security.html#web.security.oauth2.server)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [SpringDoc OpenAPI](https://springdoc.org/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/using/devtools.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/actuator/index.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Spring Cache Abstraction](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/io/caching.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/specification/configuration-metadata/annotation-processor.html)
* [Java Mail Sender](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/io/email.html)
* [Prometheus](https://docs.spring.io/spring-boot/4.0.8-SNAPSHOT/reference/actuator/metrics.html#actuator.metrics.export.prometheus)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [SpringDoc OpenAPI](https://github.com/springdoc/springdoc-openapi-demos/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Running Operational Fleet, Compliance, Predictive Maintenance & Routing Intelligence Tests (V53–V60)
To execute the focused fleet orchestration, zero-trust, predictive diagnostics, routing, dispatch simulation, fuel optimization, EV battery, and ESG carbon accounting integration test suites:
```powershell
.\mvnw.cmd test -Dtest=OtaDeploymentTest,DeviceDiagnosticTest,RemoteCommandTest,DeviceComplianceTest,ZeroTrustAttestationTest,ConfigProfileTest,FleetPredictiveMaintenanceTest,FailurePrognosticsTest,ReliabilityEngineeringTest,RouteOptimizationTest,CarbonFootprintTest,RouteCostMinimizerTest,DispatchOptimizationTest,RouteSimulationTest,DispatchConstraintSolverTest,FuelOptimizationTest,EcoDrivingTest,FuelTelemetryTest,EvEnergyTest,BatteryHealthTest,ChargingSchedulerTest,Scope1EmissionsTest,Scope2EmissionsTest,EsgComplianceTest
```


