# rn-nomba-terminal-actions

React Native module for Nomba terminal actions

## Installation

```sh
npm install rn-nomba-terminal-actions
```

## Usage

```js
import { handleTerminalRequest } from 'rn-nomba-terminal-actions';

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

      if (amount !== undefined) {
        const res: responseMap = await handleTerminalRequest([
          action,
          amount,
          '1234567890',
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
