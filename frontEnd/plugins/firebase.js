import firebase from 'firebase'
const firebaseConfig = {
    apiKey: "AIzaSyAOGhGg-6AAeRp-2PciPsvyXDDaooamhhU",
    authDomain: "gestorefarmaciechat.firebaseapp.com",
    databaseURL: "https://gestorefarmaciechat.firebaseio.com",
    projectId: "gestorefarmaciechat",
    storageBucket: "gestorefarmaciechat.appspot.com",
    messagingSenderId: "781009942511",
    appId: "1:781009942511:web:86661eb9ed89f965e9681a"
  };
if (!firebase.apps.length) {
  firebase.initializeApp(firebaseConfig)
}

export default firebase
