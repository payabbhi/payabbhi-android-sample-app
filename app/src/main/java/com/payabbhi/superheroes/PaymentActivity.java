package com.payabbhi.superheroes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.payabbhi.Payabbhi;
import com.payabbhi.PaymentCallback;
import com.payabbhi.PaymentResponse;

import org.json.JSONObject;

public class PaymentActivity extends Activity implements PaymentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;

        Payabbhi.preload(getApplicationContext());

        /* Replace 100 with order amount in paisa (e.g., 5000 paisa = Rs 50.00) */
        final double orderAmount = 100;

        Button checkoutButton = findViewById(R.id.button);

        checkoutButton.setText(String.format("Pay ₹ %.2f", orderAmount/100));
        checkoutButton.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
        checkoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Payabbhi payabbhi = new Payabbhi();

                try {
                    JSONObject options = new JSONObject();

                    /**
                    * It is expected that the Mobile Backend (server-side code) would create an Order using Payabbhi [Order API](/docs/api/#create-an-order)
                    * and pass the unique `order_id` to the App.
                    * This unique `order_id` should be passed by your App to the `Payabbhi` object to initiate Checkout.
                    */
                    options.put("order_id", "your_order_id");

                    options.put("amount", orderAmount);

                    options.put("name", "Superheroes");
                    JSONObject prefill = new JSONObject();

                    /* Give your contact and email details here */
                    prefill.put("contact", "9999999999");
                    prefill.put("email", "bruce@wayneinc.com");
                    options.put("prefill", prefill);

                    /* Set optional notes */
                    JSONObject notes = new JSONObject();
                    notes.put("Source","Android Superheroes");
                    options.put("notes", notes);

                    payabbhi.open(activity, options);
                } catch(Exception e) {
                    /* Handle any error that occurred while opening the checkout form */
                    Log.e("Error", "Error in initiating payment", e);
                    Toast.makeText(PaymentActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(PaymentResponse paymentResponse) {
        /**
         * Add your logic here to process the Payment response on successful payment
         * It is expected that you would pass the Payment response
         * to your Mobile Backend (Server-side code) for further verification.
         * Refer https://payabbhi.com/docs/integration/#verification-of-payment-response to see how to verify payment response.
         */

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Transaction successful")
                .setMessage("Payment ID: " + paymentResponse.getPaymentID())
                .setPositiveButton(R.string.alert_button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();

        Log.i("Payment ID", paymentResponse.getPaymentID());
        Log.i("Order ID", paymentResponse.getOrderID());
        Log.i("Payment Signature", paymentResponse.getPaymentSignature());
    }

    @Override
    public void onPaymentError(int code, String errorMessage) {
        /**
         * Add your logic here to handle scenarios where the Checkout did not result in a successful Payment
         */
        if (Payabbhi.NETWORK_ERROR == code) {
            Toast.makeText(PaymentActivity.this, getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
        } else if (Payabbhi.INVALID_ARGUMENTS == code) {
            Toast.makeText(PaymentActivity.this, getString(R.string.invalid_options_message), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
