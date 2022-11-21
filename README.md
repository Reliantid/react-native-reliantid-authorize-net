# React Native Authorize.net bindings

## Purpose

`react-native-reliantid-authorize-net` allows you to:

- Fetch token by sending `card detail` to `AcceptSDK` for iOS and Android. You need to use this token to get the values of `DATA DESCRIPTOR` and `DATA VALUE` from the token.

## Getting started

`$ yarn add react-native-reliantid-authorize-net`

### Installation

note: if for some reason you need manual linging use version 1.2 of this package

#### Android

Package is using autolinking.

#### iOS

Package is using autolinking with Cocoapods.

```
pod install
```

## Usage

import RNAuthorizeNet from 'react-native-reliantid-authorize-net';

### Keys used in parameter[cardValues] and the purpose of the keys:

#### Required keys:

    LOGIN_ID : "login_id of authorize.net in which the card will be added."
    CLIENT_KEY : "client_key of authorize.net in which the card will be added."
    CARD_NO : "card no"
    EXPIRATION_MONTH : "expiration month of the card."
    EXPIRATION_YEAR : "expiration year of the card."
    CVV_NO : "cvv no of the card."

### Keys used in response [responseObject] and the purpose of the keys: This will be used to add card on server using accept.js of auhorize.net

#### Response Object Keys:

    DATA_DESCRIPTOR = "card data descriptor"
    DATA_VALUE = "card data value"

## RNAuthorizeNet API Documentation

| Method                     | Method Description                                                                                                                                                                                           | Parameters               | Parameters Description                                                                                                                                                    |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| getTokenWithRequestForCard | This method is used to get response object {with `data value` and `data descriptor`} and success {bool value to show if the card added successfully} for your credit card, you want to add to authorize.net. | cardValues, isProduction | `cardValues`: the card detail object, which can hold the values with keys as described above. `isProduction`: this indicate if the processing is done for production mode |

### Contributor

- [Peter Machowski](mailto:peter@reliantid.com)
