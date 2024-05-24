package com.rnnombaterminalactions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.module.annotations.ReactModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@ReactModule(name = RnNombaTerminalActionsModule.NAME)
public class RnNombaTerminalActionsModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private Promise mPromise;
  public static final String NAME = "RnNombaTerminalActions";
  private static final int ARGS_TRANSACTION_REQUEST_CODE = 1943;
  private static final int ARGS_PRINT_RECEIPT_EVENT = 1944;
  private static final String AMOUNT_DATA = "amount";
  private static final String MERCHANT_TX_REF = "merchantTxRef";
  private static final String RECEIPT_OPTIONS = "receiptOptions";
  private static final String TXN_RESULT = "txnResultData";
  private static final String PRINT_RESULT = "PRINT_RESULT";
  private static final String PAY_BY_TRANSFER_INTENT = "com.nomba.pro.feature.pay_by_transfer.ACTION_VIEW";
  private static final String CARD_AND_PBT_INTENT = "com.nomba.pro.feature.payment_option.ACTION_VIEW";
  private static final String PRINT_CUSTOM_RECEIPT_INTENT = "com.nomba.pro.core.print_receipt.ACTION_VIEW";
  private static final String CARD_PAYMENT = "com.nomba.pro.feature.payment_option.ACTION_VIEW";
  private static final String ARGS_PAYMENT_OPTION_STATE = "ARGS_PAYMENT_OPTION_STATE";
  private static final String SDK_PAYMENT_OPTIONS = "SDK_PAYMENT_OPTIONS";
  private static final String ARGS_PRINT_DATA = "ARGS_PRINT_DATA";
  private static final String ARGS_PRINT_BITMAP_DATA = "ARGS_PRINT_BITMAP_DATA";

  public RnNombaTerminalActionsModule(ReactApplicationContext reactContext) {
    super(reactContext);

    reactContext.addActivityEventListener(this);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void handleTerminalRequest(final ReadableArray args, Promise promise) throws Exception {
    this.mPromise = promise;

    final var actionKey = args.getString(0);
    final var isPayment = !Objects.equals(actionKey,
      "triggerPrintCustomReceipt") && !Objects.equals(
      actionKey,
      "getDeviceInfo");
    final var amount = isPayment ? args.getString(1) : "";
    final var transactionReference = isPayment ? args.getString(2) : "";
    final var receiptData = isPayment ? args.getString(3) : "";

    switch (actionKey) {
      case "triggerCardPayment":
        this.triggerPayment(CARD_PAYMENT, amount, transactionReference, receiptData);
        break;
      case "triggerPayByTransfer":
        this.triggerPayment(PAY_BY_TRANSFER_INTENT,
          amount,
          transactionReference,
          receiptData);
        break;
      case "triggerCardAndPBT":
        this.triggerCardAndPBT(amount, transactionReference, receiptData);
        break;
      case "triggerPrintCustomReceipt":
        this.triggerPrintCustomReceipt(args.getString(1), args.getString(2));
        break;
      default:
        this.handleResponse("Invalid terminal action", "failed");
    }
  }

  private void triggerPayment(final String intentAction,
                              final String amount,
                              final String transactionReference,
                              final String receiptData) {
    try {
      final var intent = new Intent(intentAction);
      final var formattedAmount = Integer.parseInt(amount) * 100;
      intent.putExtra(AMOUNT_DATA, String.valueOf(formattedAmount));
      intent.putExtra(MERCHANT_TX_REF, transactionReference);

      intent.putExtra(RECEIPT_OPTIONS, this.getReceiptOptions(receiptData));

      Objects.requireNonNull(this.getCurrentActivity()).startActivityForResult(intent,
        ARGS_TRANSACTION_REQUEST_CODE);
    } catch (final Exception e) {
      this.handleError("Failed to complete payment action", e);
    }
  }

  private void triggerCardAndPBT(final String amount,
                                 final String transactionReference,
                                 final String receiptData) {
    try {
      final var intent = new Intent(CARD_AND_PBT_INTENT);

      final var formattedAmount = Integer.parseInt(amount) * 100;
      intent.putExtra(AMOUNT_DATA, String.valueOf(formattedAmount));
      intent.putExtra(MERCHANT_TX_REF, transactionReference);
      intent.putExtra(RECEIPT_OPTIONS, this.getReceiptOptions(receiptData));
      intent.putExtra(ARGS_PAYMENT_OPTION_STATE, SDK_PAYMENT_OPTIONS);

      Objects.requireNonNull(this.getCurrentActivity()).startActivityForResult(intent,
        ARGS_TRANSACTION_REQUEST_CODE);
    } catch (final Exception e) {
      this.handleError("Failed to complete transfer action", e);
    }
  }

  private void triggerPrintCustomReceipt(final String receiptData, final String logoPath) {
    try {
      final var intent = new Intent(PRINT_CUSTOM_RECEIPT_INTENT);
      intent.putExtras(this.createPrintReceiptBundle(receiptData, logoPath));

      Objects.requireNonNull(this.getCurrentActivity()).startActivityForResult(intent,
        ARGS_PRINT_RECEIPT_EVENT);
    } catch (final Exception e) {
      this.handleError("Failed to print custom receipt", e);
    }
  }

  private Bundle createPrintReceiptBundle(final String receiptData, final String logoPath) {
    final var gson = new Gson();
    final var type = new TypeToken<ArrayList<HashMap<String, Object>>>() {
    }.getType();
    final ArrayList<HashMap<String, Object>> arrayList = gson.fromJson(receiptData, type);

    final var bundle = new Bundle();
    bundle.putSerializable(ARGS_PRINT_DATA, arrayList);

    final var receiptLogo = this.bitmapFromFilePath(logoPath);

    bundle.putParcelable(ARGS_PRINT_BITMAP_DATA, receiptLogo);

    return bundle;
  }

  private Bitmap bitmapFromFilePath(final String filePath) {
    return BitmapFactory.decodeFile(filePath);
  }

  private void handleError(final String error, final Exception exception) {
    this.handleResponse(error + ":: " + exception.getMessage(), "failed");
  }

  private String getReceiptOptions(final String receiptData) {
    final var gson = new Gson();
    final var type = new TypeToken<HashMap<String, Object>>() {
    }.getType();
    final HashMap<String, Object> hashMap = gson.fromJson(receiptData, type);
    return gson.toJson(hashMap);
  }

  @ReactMethod
  public void addListener(final String eventName) {

  }

  @ReactMethod
  public void removeListeners(final Integer count) {

  }

  private void handleResponse(final String message,
                              final String status) {
    if (this.mPromise != null) {
      if (Objects.equals(status, "failed")) {
        this.mPromise.reject("error", message);
      } else {
        final var map = Arguments.createMap();
        map.putString("result", message);
        this.mPromise.resolve(map);
      }

      this.mPromise = null;
    }
  }

  @Override
  public void onActivityResult(final Activity activity,
                               final int i,
                               final int i1,
                               @Nullable final Intent intent) {
    if (i == ARGS_TRANSACTION_REQUEST_CODE) {
      if (i1 == Activity.RESULT_OK) {
        if (intent != null) {
          RnNombaTerminalActionsModule.this.handleResponse(intent.getStringExtra(TXN_RESULT) != null ? intent.getStringExtra(
              TXN_RESULT) : "Empty result",
            "success");
        } else {
          RnNombaTerminalActionsModule.this.handleResponse("action failed to complete",
            "failed");
        }
      } else if (i1 == Activity.RESULT_CANCELED) {
        if (intent != null) {
          RnNombaTerminalActionsModule.this.handleResponse(intent.getStringExtra(TXN_RESULT) != null ? intent.getStringExtra(
              TXN_RESULT) : "Empty result",
            "transactionCancelled");
        } else {
          RnNombaTerminalActionsModule.this.handleResponse("action failed to complete",
            "failed");
        }
      } else {
        RnNombaTerminalActionsModule.this.handleResponse("action failed to complete", "failed");
      }
    } else {
      RnNombaTerminalActionsModule.this.handleResponse("action failed to complete", "failed");
    }
  }

  @Override
  public void onNewIntent(final Intent intent) {

  }
}
