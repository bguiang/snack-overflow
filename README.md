# SnackOverflow-Web

Requirements for Deployment to Heroku:

Deploying React Frontend as part of the Spring WebApp
- use localhost:8080 if running locally or use the webapp base url if production
- run "npm run build" to execute the build script. This will create a "build" folder inside the react project folder ("src/main/frontend-react/build")
- copy the content of the build folder and paste it into the Spring's static resources folder ("src/main/resources/static")
