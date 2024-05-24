import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { handleTerminalRequest } from 'rn-nomba-terminal-actions';

type responseMap = {
  [key: string]: string;
};

export default function App() {
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

  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>Nomba Terminal Actions</Text>
      <Text style={styles.instructions}>
        From our Native platform:: {result}
      </Text>
      <View style={styles.buttonContainer}>
        <Button
          onPress={() => onHandleTerminalRequest('card_payment_action')}
          title="Run Card Payment Action"
          color="#841584"
          accessibilityLabel="Run card payment button"
        />
      </View>
      <View style={styles.buttonContainer}>
        <Button
          onPress={() => onHandleTerminalRequest('pay_by_transfer_action')}
          title="Run Pay by Transfer Action"
          color="#841584"
          accessibilityLabel="Run pay by transfer button"
        />
      </View>
      <View style={styles.buttonContainer}>
        <Button
          onPress={() => onHandleTerminalRequest('card_payment_and_PBT_action')}
          title="Run Pay by Card + Transfer Action"
          color="#841584"
          accessibilityLabel="Run pay by transfer button"
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  buttonContainer: {
    marginBottom: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
