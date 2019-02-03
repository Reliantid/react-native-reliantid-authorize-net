
package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

import java.util.HashMap;

public class RNAuthorizeNetModule extends ReactContextBaseJavaModule {

  static String CARD_NO = "CARD_NO";
  static String EXPIRATION_MONTH = "EXPIRATION_MONTH";
  static String EXPIRATION_YEAR = "EXPIRATION_YEAR";
  static String CVV_NO = "CVV_NO";
  static String ZIP_CODE = "ZIP_CODE";
  static String CARD_HOLDER_NAME = "CARD_HOLDER_NAME";
  static String LOGIN_ID = "LOGIN_ID";
  static String CLIENT_KEY = "CLIENT_KEY";
  static String ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
  static String ACCOUNT_HOLDER_EMAIL = "ACCOUNT_HOLDER_EMAIL";
  static String DATA_DESCRIPTOR = "DATA_DESCRIPTOR";
  static String DATA_VALUE = "DATA_VALUE";
  private final ReactApplicationContext reactContext;
  private static Activity mCurrentActivity = null;

  AcceptSDKApiClient apiClient;


  public CardData prepareCardDataFromFields(ReadableMap cardValue){
    CardData cardData = new CardData.Builder(cardValue.getString(CARD_NO),
            cardValue.getString(EXPIRATION_MONTH),
            cardValue.getString(EXPIRATION_YEAR))
            .cvvCode(cardValue.getString(CVV_NO)) // Optional
            //.zipCode(ZIP_CODE)// Optional
            //.cardHolderName(CARD_HOLDER_NAME)// Optional
            .build();
    return cardData;
  }




  private EncryptTransactionObject prepareTransactionObject(ReadableMap cardValue) {
    ClientKeyBasedMerchantAuthentication merchantAuthentication =
            ClientKeyBasedMerchantAuthentication.
                    createMerchantAuthentication(cardValue.getString(LOGIN_ID), cardValue.getString(CLIENT_KEY));

    // create a transaction object by calling the predefined api for creation
    return TransactionObject.
            createTransactionObject(
                    TransactionType.SDK_TRANSACTION_ENCRYPTION) // type of transaction object
            .cardData(prepareCardDataFromFields(cardValue)) // card data to get Token
            .merchantAuthentication(merchantAuthentication).build();
  }


  public RNAuthorizeNetModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

  }





  @Override
  public String getName() {
    return "RNAuthorizeNet";
  }

  @ReactMethod
  public void getTokenWithRequestForCard(ReadableMap cardValue,boolean isProduction, final Callback responseCallBack){
    try {
      if(isProduction == true){
        apiClient = new AcceptSDKApiClient.Builder (reactContext,
                AcceptSDKApiClient.Environment.PRODUCTION)
                .connectionTimeout(5000) // optional connection time out in milliseconds
                .build();
      }else{
        apiClient = new AcceptSDKApiClient.Builder (reactContext,
                AcceptSDKApiClient.Environment.SANDBOX)
                .connectionTimeout(5000) // optional connection time out in milliseconds
                .build();
      }
      EncryptTransactionObject transactionObject = prepareTransactionObject(cardValue);
      apiClient.getTokenWithRequest(transactionObject, new EncryptTransactionCallback() {
        @Override
        public void onErrorReceived(ErrorTransactionResponse error) {
          responseCallBack.invoke(false,"Error while add card.");
        }

        @Override
        public void onEncryptionFinished(EncryptTransactionResponse response) {
          WritableMap cardResponse = Arguments.createMap();
          cardResponse.putString(DATA_DESCRIPTOR,response.getDataDescriptor());
          cardResponse.putString(DATA_VALUE,response.getDataValue());
          responseCallBack.invoke(true,cardResponse);
        }
      });

    } catch (Exception e) {
      // Handle exception transactionObject or callback is null.
      responseCallBack.invoke(false,"Error while add card.");
    }
  }


}