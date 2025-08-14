# rn-nomba-terminal-sdk

React Native module for Nomba terminal SDK actions

## Installation

Using npm
```sh
npm install rn-nomba-terminal-sdk
```
or using yarn
```sh
yarn add rn-nomba-terminal-sdk
```

## Usage

```js
import { handleTerminalRequest } from 'rn-nomba-terminal-sdk';

// ...

type responseMap = {
  [key: string]: string;
};

const [result, setResult] = React.useState<any>();

  function isErrorWithMessage(error: any): error is { message: string } {
    return error && typeof error.message === 'string';
  }

  const getAmount = (amount: string) => {
    const amountInCents = parseInt(amount, 10);

    if (isNaN(amountInCents)) {
      console.error('Invalid amount entered.');
      return undefined;
    }

    return amountInCents.toString();
  };

  const onHandleTerminalRequest = async (action: string) => {
    try {
      var receiptData = {
        email: false,
        sms: true,
        print: true,
      };

      let amount: string | undefined = getAmount('2');

      let merchantTransactionReference = '1234567890';

      if (amount !== undefined) {
        const res: responseMap = await handleTerminalRequest([
          action,
          amount,
          merchantTransactionReference,
          JSON.stringify(receiptData),
        ]);

        setResult(res.result);
      }
    } catch (e: any) {
      if (isErrorWithMessage(e)) {
        setResult(e.message);
      } else {
        setResult(e);
      }
    }
  };

```

|       ACTIONS |VALUES                          |PROPERTIES                         |
|----------------|-------------------------------|-----------------------------|
|Card Payments|`card_payment_action`            |[`card_payment_action`, amount, merchantTransactionReference, receiptData]            |
|Pay by Transfer         |`pay_by_transfer_action`            |[`pay_by_transfer_action`, amount, merchantTransactionReference, receiptData]           |
|Card Payments & Pay by Transfers          |`card_payment_and_pbt_action`|[`card_payment_and_pbt_action`, amount, merchantTransactionReference, receiptData]|

## Expo (SDK 51) Support

This package includes an **Expo Config Plugin** so it works in Expo managed projects. Because it contains native code, it **cannot run in the stock Expo Go app**. Use a **Development Build** for local dev or **EAS Build** for production.

**Install in an Expo app**
```bash
expo install rn-nomba-terminal-sdk
```
