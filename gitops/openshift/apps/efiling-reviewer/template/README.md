## Templates to create openshift components related to efiling-reviewer api deployment

### Command to execute template
1) Login to OC using login command
2) Run below command in each env. namespace dev/test/prod
   ``oc process -f efiling-reviewer.yaml --param-file=efiling-reviewer.env | oc apply -f -``
