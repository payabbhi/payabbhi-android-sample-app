# Payabbhi Android Sample App

This is a reference app for enabling Payments acceptance in your Android App using [Payabbhi Android SDK](https://payabbhi.com/docs/mobile-checkout/android).

For Integration guide, refer to [Mobile Checkout - Android](https://payabbhi.com/docs/mobile-checkout/android).

### Running the Sample app

Make sure you have signed up for your [Payabbhi Account](https://payabbhi.com/docs/account) and downloaded the [API keys](https://payabbhi.com/docs/account/#api-keys) from the [Portal](https://payabbhi.com/portal).

- Clone the Android Sample App repository.

- Replace `your_access_id` with your `Access ID` in `AndroidManifest.xml`

- Add logic to get `order_id` from your Mobile Backend (server-side code) in `PaymentActivity.java`

    > TIP: An alternative to having a Mobile Backend for running the sample App is:
    > 1. Generate a unique order_id using curl
    > 2. Copy/paste the generated order_id in `PaymentActivity.java`
    > 3. Once a successful test transaction is completed for a particular order, repeat the above steps

    ```
    curl https://payabbhi.com/api/v1/orders \
      -u access_id:secret_key \
      -d amount=100 \
      -d merchant_order_id=ordRefNo123456 \
      -d currency=INR
    ```


- Pass suitable values in Checkout options in `PaymentActivity.java` e.g. `orderAmount` as per Order.amount
