import firebase from 'firebase/compat/app';
import 'firebase/firestore';
import 'firebase/compat/auth';

// eslint-disable-next-line @typescript-eslint/no-var-requires
const firebaseInfo = require('./resouces/config');

const firebaseConfig = {
  apiKey: firebaseInfo.firebase.api_key,
  authDomain: firebaseInfo.firebase.auth_domain,
  projectId: firebaseInfo.firebase.priject_id,
  storageBucket: firebaseInfo.firebase.storage_bucket,
  messagingSenderId: firebaseInfo.firebase.mesage_sender_id,
  appId: firebaseInfo.firebase.app_id,
};

firebase.initializeApp(firebaseConfig);

export const auth = firebase.auth();
