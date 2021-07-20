# SnackOverflow

# Environment Setup

The React frontend stores the stripe public api key in src/main/frontend/.env but is gitignored
- create a ".env" file in the root folder of the react frontend and add a variable called "REACT_APP_STRIPE_PUBLIC_KEY" and set it equal to your Stripe publishable key

The Spring Boot backend also expects environment variables for:
- "stripe_access_key" which should have the value of your Stripe secret key
- "unsplash_access_token" which is your unsplash api key
- "stripe_webhook_secret" from your stripe developer dashboard. Used for verifying payment intent events are from Stripe

Stripe CLI
- Orders status can only be updated by an admin or through the stripe webhook. Stripe cannot send payment intent event messages to the webhook when running locally but it can be done by listening to the events with the Stripe CLI and forwarding them to the local server https://stripe.com/docs/stripe-cli/webhooks