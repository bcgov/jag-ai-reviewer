# new feature
# Tags: optional

Feature: Extract notifications from CSO api

  Scenario: Validate CSO notifications can be extracted
    Given document details are posted to notification api
    Then valid response is returned
