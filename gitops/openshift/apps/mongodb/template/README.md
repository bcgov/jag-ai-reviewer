## Templates to create openshift components related to mongodb & mongoexpress deployment

### Command to execute template
1) Login to OC using login command
2) Run below command in each env. namespace dev/test/prod
   ``oc process -f mongodb.yaml --param-file=mongodb.env | oc apply -f -``
3) Run below command in each env. namespace dev/test/prod
   ``oc process -f mongo-express.yaml --param-file=mongo-express.env | oc apply -f -``
